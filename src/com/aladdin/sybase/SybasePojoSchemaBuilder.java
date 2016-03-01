package com.aladdin.sybase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.aladdin.database.DatabaseHelper;
import com.aladdin.entities.ColumnType;
import com.aladdin.sybase.SybaseTableIndexInfo.IndexType;
import com.aladdin.sybase.schema.mapper.xml.Column;
import com.aladdin.sybase.schema.mapper.xml.SchemaMapping;
import com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase;
import com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database.Tables;
import com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database.Tables.Table.AffinityKey;
import com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database.Tables.Table.ExcludedColumns;
import com.aladdin.sybase.schema.mapper.xml.Serializer;

public class SybasePojoSchemaBuilder
{
	private static final String DOUBLE_LINE_SPLITTER = "\n\n";

	private final static Logger LOG = LogManager.getLogger(SybasePojoSchemaBuilder.class);

	private final static class Config
	{
		final SybaseConfig sybase;

		Config(SchemaMapping config)
		{
			this.sybase = new SybaseConfig(config.getSybase());
		}

		@Override
		public String toString()
		{
			return "Config [sybase=" + this.sybase + "]";
		}

		final class SybaseConfig
		{
			final Map<String, Database> databases;

			SybaseConfig(Sybase sybase)
			{
				this.databases = new HashMap<>();

				for(com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database database : sybase.getDatabases().getDatabase())
					this.databases.put(database.getName(), new Database(database));
			}

			@Override
			public String toString()
			{
				return "SybaseConfig [databases=" + this.databases + "]";
			}

			private abstract class AbstractColumn
			{
				final String name;

				protected AbstractColumn(Column column)
				{
					this.name = column.getName();
				}
			}

			final class Database
			{
				final String name;

				final Map<String, Table> tables;

				final boolean mapAllTables;

				Database(com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database database)
				{

					Tables tables = database.getTables();
					this.mapAllTables = tables.isMapAll();
					this.name = database.getName();
					this.tables = new HashMap<>();

					for(com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database.Tables.Table table : tables.getTable())
					{
						Table tbl = new Table(table);
						this.tables.put(tbl.name, tbl);
					}
				}

				@Override
				public String toString()
				{
					return "Database [name=" + this.name + ", tables=" + this.tables + ", mapAllTables=" + this.mapAllTables + "]";
				}

				final class Table
				{
					final String className;

					final String name;

					final String pkIndexName;

					final CacheWriteSynchronizationMode writeSynchMode;

					final CacheRebalanceMode rebalMode;

					final int backups;

					final CacheAtomicityMode cacheAtomicityMode;

					final CacheMode cacheMode;

					final Map<String, ExcludedColumn> excludedColumns;

					final Map<String, AffinityColumn> affinityColumns;

					Table(com.aladdin.sybase.schema.mapper.xml.SchemaMapping.Sybase.Databases.Database.Tables.Table table)
					{
						this.pkIndexName = table.getPkIndexName();
						this.name = table.getName();
						this.className = table.getClassName();
						this.writeSynchMode = CacheWriteSynchronizationMode.valueOf(table.getWriteSynchMode());
						this.backups = table.getBackups();
						this.cacheMode = CacheMode.valueOf(table.getCacheMode());
						this.rebalMode = CacheRebalanceMode.valueOf(table.getRebalMode());
						this.cacheAtomicityMode = CacheAtomicityMode.valueOf(table.getCacheAtomicityMode());
						this.excludedColumns = new HashMap<>();

						ExcludedColumns excludedColumns = table.getExcludedColumns();

						if(excludedColumns != null)
							for(Column excludedColumn : excludedColumns.getColumn())
								this.excludedColumns.put(excludedColumn.getName(), new ExcludedColumn(excludedColumn));

						AffinityKey affinity = table.getAffinityKey();

						if(affinity == null)
							this.affinityColumns = null;
						else
						{
							this.affinityColumns = new HashMap<>();

							for(Column affinityColumn : affinity.getColumn())
								this.affinityColumns.put(affinityColumn.getName(), new AffinityColumn(affinityColumn));
						}
					}

					public String getWriteSynchMode()
					{
						return "CacheWriteSynchronizationMode." + this.writeSynchMode.name();
					}

					public String getRebalMode()
					{
						return "CacheRebalanceMode." + this.rebalMode.name();
					}

					public String getCacheAtomicityMode()
					{
						return "CacheAtomicityMode." + this.cacheAtomicityMode.name();
					}

					public String getCacheMode()
					{
						return "CacheMode." + this.cacheMode.name();
					}

					@Override
					public String toString()
					{
						return "Table [name=" + this.name + ", excludedColumns=" + this.excludedColumns + "]";
					}

					final class ExcludedColumn extends AbstractColumn
					{
						ExcludedColumn(Column column)
						{
							super(column);
						}
					}

					private final class AffinityColumn extends AbstractColumn
					{
						AffinityColumn(Column column)
						{
							super(column);
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws SQLException, IOException, JAXBException
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		Config config = new Config(Serializer.deserialize(new File(System.getProperty("aladdin.sybase.mapping.config"))));
		DatabaseHelper db = DatabaseHelper.getInstance();

		for(Config.SybaseConfig.Database database : config.sybase.databases.values())
		{
			Map<String, SybaseTableSchema> tableMap = new HashMap<>();
			if(database.name.equals("gpxdb"))
				database.toString();

			Statement stmt = (database.name.equals("gpxdb") ? db.getGPXConnection() : db.getReadConnection()).statement();
			stmt.execute("USE " + database.name);
			ResultSet result = stmt.executeQuery("SELECT t.name as table_name, c.name as column_name, c.type, c.status, c.length\r\n" +
					"FROM sysobjects t\r\n" +
					"INNER JOIN syscolumns c\r\n" +
					"    ON c.id = t.id\r\n" +
					"WHERE t.type = 'U'");

			while(result.next())
			{
				String tableName = result.getString("table_name");
				String columnName = result.getString("column_name");
				Config.SybaseConfig.Database.Table tableConfig = database.tables.get(tableName);

				if(tableName.equals("rs_lastcommit") || tableName.equals("rs_threads") // SYBASE TABLE CHECK
						|| tableName.equals("rs_dbversion") || tableName.equals("rs_ticket_history") // SYBASE TABLE CHECK
						|| (!database.mapAllTables && (tableConfig == null)) // IF NOT MAPPING ALL TABLES AND TABLE NAME NOT BEING MAPPED
						|| ((tableConfig != null) && tableConfig.excludedColumns.containsKey(columnName))) // IF COLUMN IS EXCLUDED IGNORE IT
					continue;

				SybaseTableSchema table = tableMap.get(tableName);

				if(table == null)
					tableMap.put(tableName, table = new SybaseTableSchema(tableName, new LinkedHashMap<>()));

				table.getColMap().put(columnName,
						new SybaseTableColumn(table, columnName, result.getInt("type"), result.getShort("status"), result.getInt("length")));
			}

			ResultSet rs = stmt.executeQuery("select o.name table_name,  i.name index_name,\r\n" +
					"    index_col(o.name, i.indid, 1) as IdxCol1,\r\n" +
					"    case when i.keycnt > 1 then index_col(o.name, i.indid, 2) else null end IdxCol2,\r\n" +
					"    case when i.keycnt > 2 then index_col(o.name, i.indid, 3) else null end IdxCol3,\r\n" +
					"    case when i.keycnt > 3 then index_col(o.name, i.indid, 4) else null end IdxCol4,\r\n" +
					"    case when i.keycnt > 4 then index_col(o.name, i.indid, 5) else null end IdxCol5,\r\n" +
					"    case when i.keycnt > 5 then index_col(o.name, i.indid, 6) else null end IdxCol6,\r\n" +
					"    case when i.keycnt > 6 then index_col(o.name, i.indid, 7) else null end IdxCol7,\r\n" +
					"    case when i.keycnt > 7 then index_col(o.name, i.indid, 8) else null end IdxCol8,\r\n" +
					"    case when i.keycnt > 8 then index_col(o.name, i.indid, 9) else null end IdxCol9,\r\n" +
					"    case when i.keycnt > 9 then index_col(o.name, i.indid, 10) else null end IdxCol10,\r\n" +
					"    case when i.keycnt > 10 then index_col(o.name, i.indid, 11) else null end IdxCol11,\r\n" +
					"    case when i.keycnt > 11 then index_col(o.name, i.indid, 12) else null end IdxCol12,\r\n" +
					"    case when i.keycnt > 12 then index_col(o.name, i.indid, 13) else null end IdxCol13,\r\n" +
					"    case when i.keycnt > 13 then index_col(o.name, i.indid, 14) else null end IdxCol14,\r\n" +
					"    case when i.keycnt > 14 then index_col(o.name, i.indid, 15) else null end IdxCol15,\r\n" +
					"    case when i.keycnt > 15 then index_col(o.name, i.indid, 16) else null end IdxCol16,\r\n" +
					"    case when i.keycnt > 16 then index_col(o.name, i.indid, 17) else null end IdxCol17,\r\n" +
					"    case when i.keycnt > 17 then index_col(o.name, i.indid, 18) else null end IdxCol18,\r\n" +
					"    case when i.keycnt > 18 then index_col(o.name, i.indid, 19) else null end IdxCol19,\r\n" +
					"    case when i.keycnt > 19 then index_col(o.name, i.indid, 20) else null end IdxCol20,\r\n" +
					"    case when i.keycnt > 20 then index_col(o.name, i.indid, 21) else null end IdxCol21,\r\n" +
					"    case when i.keycnt > 21 then index_col(o.name, i.indid, 22) else null end IdxCol22,\r\n" +
					"    case when i.keycnt > 22 then index_col(o.name, i.indid, 23) else null end IdxCol23,\r\n" +
					"    case when i.keycnt > 23 then index_col(o.name, i.indid, 24) else null end IdxCol24,\r\n" +
					"    case when i.keycnt > 24 then index_col(o.name, i.indid, 25) else null end IdxCol25,\r\n" +
					"    case when i.keycnt > 25 then index_col(o.name, i.indid, 26) else null end IdxCol26,\r\n" +
					"    case when i.keycnt > 26 then index_col(o.name, i.indid, 27) else null end IdxCol27,\r\n" +
					"    case when i.keycnt > 27 then index_col(o.name, i.indid, 28) else null end IdxCol28,\r\n" +
					"    case when i.keycnt > 28 then index_col(o.name, i.indid, 29) else null end IdxCol29,\r\n" +
					"    case when i.keycnt > 29 then index_col(o.name, i.indid, 30) else null end IdxCol30,\r\n" +
					"    case when i.keycnt > 30 then index_col(o.name, i.indid, 31) else null end IdxCol31,\r\n" +
					"    case when i.keycnt > 31 then index_col(o.name, i.indid, 32) else null end IdxCol32,\r\n" +
					"    case when i.status2 & 2 = 2 then 'PX' else (case when i.status & 2 = 2 then 'UX' else 'IX' end) end type\r\n" +
					"from sysobjects o, sysindexes i\r\n" +
					"where o.id = i.id and o.type = 'U' and i.indid > 0 and index_col(o.name, i.indid, 1) != null");

			while(rs.next())
			{
				String tableName = rs.getString("table_name");
				SybaseTableSchema table = tableMap.get(tableName);

				if(table == null)
					continue;

				SybaseTableIndexInfo index = new SybaseTableIndexInfo(table, rs.getString("index_name"), IndexType.valueOf(rs.getString("type")),
						rs.getString("IdxCol1"),
						rs.getString("IdxCol2"), rs.getString("IdxCol3"), rs.getString("IdxCol4"), rs.getString("IdxCol5"), rs.getString("IdxCol6"),
						rs.getString("IdxCol7"), rs.getString("IdxCol8"), rs.getString("IdxCol9"), rs.getString("IdxCol10"), rs.getString("IdxCol11"),
						rs.getString("IdxCol12"), rs.getString("IdxCol13"), rs.getString("IdxCol14"), rs.getString("IdxCol15"), rs.getString("IdxCol16"),
						rs.getString("IdxCol17"), rs.getString("IdxCol18"), rs.getString("IdxCol19"), rs.getString("IdxCol20"), rs.getString("IdxCol21"),
						rs.getString("IdxCol22"), rs.getString("IdxCol23"), rs.getString("IdxCol24"), rs.getString("IdxCol25"), rs.getString("IdxCol26"),
						rs.getString("IdxCol27"), rs.getString("IdxCol28"), rs.getString("IdxCol29"), rs.getString("IdxCol30"), rs.getString("IdxCol31"),
						rs.getString("IdxCol32"));

				Config.SybaseConfig.Database.Table tableConfig = database.tables.get(tableName);

				if((tableConfig == null) || (tableConfig.pkIndexName == null))
				{
					SybaseTableIndexInfo tableKey = table.getKey();

					if((tableKey == null) && index.type.isKey())
						table.setKey(index);
					else if((tableKey != null) && index.type.isKey() && !tableKey.type.isPrimary()
							&& (tableKey.calculateSize(table.getColMap()) > index.calculateSize(table.getColMap())))
					{
						table.getIndexes().put(tableKey.index_name, tableKey);
						table.setKey(index);
					}
					else
						table.getIndexes().put(index.index_name, index);
				}
				else if(tableConfig.pkIndexName.equals(index.index_name))
					table.setKey(index);
				else
					table.getIndexes().put(index.index_name, index);
			}

			for(Entry<String, SybaseTableSchema> table : tableMap.entrySet())
			{
				SybaseTableSchema tableObj = table.getValue();
				Map<String, SybaseTableColumn> colMap = tableObj.getColMap();
				String tableName = table.getKey();
				StringBuilder superStr = new StringBuilder();
				superStr.append("super(");
				String keyClass;
				Config.SybaseConfig.Database.Table tableConfig = database.tables.get(tableName);
				String className = (tableConfig != null) && (tableConfig.className != null) ? tableConfig.className : ColumnType
						.convertSybaseColNameToJavaMethodName(tableName);
				String filePath = System.getProperty("aladdin.sybase.mapping.output") + "/src/com/aladdin/entities/" + database.name;

				if(tableObj.getKey() == null)
				{
					LOG.warn("No key set for " + database.name + '.' + tableObj.getTableName() + "! Not mapping this table to a POJO!");
					continue;
				}
				else if(tableObj.getKey().keys.size() == 1)
				{
					if((tableConfig != null) && (tableConfig.affinityColumns != null))
						throw new RuntimeException("You have been too lazy and haven't done this yet!");

					SybaseTableColumn col = colMap.get(tableObj.getKey().keys.get(0));
					keyClass = ColumnType.getColumnTypeFromSybaseSchema(col).getJavaClassString(col.nullable);
					superStr.append(ColumnType.convertSybaseColNameToJavaFieldName(col.columnName));
				}
				else
				{
					if((tableConfig != null) && (tableConfig.affinityColumns != null) && !tableConfig.affinityColumns.isEmpty())
						for(Config.SybaseConfig.Database.Table.AffinityColumn affinityColumn : tableConfig.affinityColumns.values())
							if(!tableObj.getKey().keys.contains(affinityColumn.name))
								throw new RuntimeException("You have been too lazy and haven't done this yet!");

					keyClass = className + "Key";
					superStr.append("new ").append(keyClass).append('(');
					StringBuilder inner = new StringBuilder();
					StringBuilder keyFields = new StringBuilder();
					StringBuilder keyGetters = new StringBuilder();
					StringBuilder keyConstructor = new StringBuilder();
					StringBuilder keyConstructorBody = new StringBuilder();
					StringBuilder keyExternalizableOut = new StringBuilder();
					StringBuilder keyExternalizableIn = new StringBuilder();
					StringBuilder keyHashCode = new StringBuilder();
					StringBuilder keyEquals = new StringBuilder().append('\t');
					TreeSet<String> keyImports = new TreeSet<>();
					keyImports.add("java.io.Externalizable");
					keyImports.add("java.io.ObjectOutput");
					keyImports.add("java.io.ObjectInput");
					keyImports.add("java.io.IOException");
					keyImports.add("org.apache.commons.lang3.builder.HashCodeBuilder");

					for(String key : tableObj.getKey().keys)
					{
						SybaseTableColumn col = colMap.get(key);
						boolean isAffinity = (tableConfig != null) && (tableConfig.affinityColumns != null) && tableConfig.affinityColumns.containsKey(key);

						if(isAffinity)
							keyImports.add("org.apache.ignite.cache.affinity.AffinityKeyMapped");

						if(inner.length() > 0)
							inner.append(", ");

						String[] keySchema = ColumnType.getJavaDefinitionFromSybaseSchema(col, true, false, isAffinity);

						if(keySchema == null)
							throw new RuntimeException("Could not map SqlType: " + col.sybaseType + " to java type for table: " + database.name + '.'
									+ tableName + " and column: " + col.columnName);
						else if(keySchema[0] != null)
							keyImports.add(keySchema[0]);

						if(keyFields.length() > 0)
						{
							keyFields.append(DOUBLE_LINE_SPLITTER);
							keyGetters.append(DOUBLE_LINE_SPLITTER);
							keyEquals.append("\n\telse ");
							keyConstructor.append(", ");
							keyConstructorBody.append('\n');
							keyExternalizableOut.append('\n');
							keyExternalizableIn.append('\n');
							keyHashCode.append('\n');
						}

						inner.append(ColumnType.convertSybaseColNameToJavaFieldName(col.columnName));
						keyFields.append(keySchema[1]);
						keyConstructor.append(keySchema[2]);
						keyConstructorBody.append(keySchema[3]);
						keyGetters.append(keySchema[4]);
						keyExternalizableOut.append(keySchema[5]);
						keyExternalizableIn.append(keySchema[6]);
						keyHashCode.append(keySchema[7]);
						keyEquals.append(keySchema[8]);
					}

					StringBuilder sb = new StringBuilder()
					.append("package com.aladdin.entities.")
					.append(database.name).append(";\n");

					for(String imp : keyImports)
						sb.append("\nimport ").append(imp).append(';');

					sb.append("\n\n").append("public final class ")
					.append(keyClass).append(" implements Externalizable\n{")
					.append("\n\tprivate static final long serialVersionUID = 1L;\n");

					keyConstructor = new StringBuilder()
							.append("public ").append(keyClass).append('(').append(keyConstructor)
							.append(")\n{\n").append(keyConstructorBody).append("\n}")
							.append(DOUBLE_LINE_SPLITTER)
							.append("@SuppressWarnings(\"unused\")//THIS IS NEEDED FOR SERIALIZATION! DO NOT DELETE OR EDIT!\nprivate ")
							.append(keyClass).append("() {}");

					StringBuilder outAndIn = new StringBuilder()
							.append("@Override\npublic void writeExternal(ObjectOutput out) throws IOException\n{\n")
							.append(keyExternalizableOut).append("\n}").append(DOUBLE_LINE_SPLITTER)
							.append("@Override\npublic void readExternal(ObjectInput in) throws IOException, ClassNotFoundException\n{\n")
							.append(keyExternalizableIn).append("\n}");

					StringBuilder hcb = new StringBuilder()
							.append("@Override\npublic int hashCode()\n{\n\tHashCodeBuilder hcb = new HashCodeBuilder();")
							.append(DOUBLE_LINE_SPLITTER).append(keyHashCode)
							.append(DOUBLE_LINE_SPLITTER).append("\treturn hcb.toHashCode();\n}");

					StringBuilder eq = new StringBuilder()
							.append("@Override\npublic boolean equals(Object obj)\n{\n")
							.append("\tif(obj == null || !(obj instanceof ").append(keyClass)
							.append("))\n\t\treturn false;").append(DOUBLE_LINE_SPLITTER)
							.append('\t').append(keyClass).append(" o = (").append(keyClass).append(")obj;")
							.append(DOUBLE_LINE_SPLITTER).append(keyEquals).append(DOUBLE_LINE_SPLITTER)
							.append("\treturn true;\n}");

					buildClassBody(sb, keyFields, keyConstructor, keyGetters, outAndIn, hcb, eq);
					sb.append('}');
					File dbFolder = new File(filePath);

					if(!dbFolder.exists())
						dbFolder.mkdirs();

					try(FileWriter writer = new FileWriter(filePath + '/' + keyClass + ".java"))
					{
						writer.write(sb.toString());
					}

					superStr.append(inner).append(')');
				}

				superStr.append(");");
				StringBuilder fields = new StringBuilder();
				StringBuilder getters = new StringBuilder();
				StringBuilder constructor = new StringBuilder();
				StringBuilder constructorBody = new StringBuilder();
				StringBuilder externalizableOut = new StringBuilder();
				StringBuilder externalizableIn = new StringBuilder();
				StringBuilder hashCode = new StringBuilder();
				StringBuilder equals = new StringBuilder().append('\t');
				constructorBody.append('\t').append(superStr).append(DOUBLE_LINE_SPLITTER);
				Set<String> imports = new TreeSet<>();
				imports.add("com.aladdin.ignite.AladdinCacheManager");
				imports.add("com.aladdin.entities.AbstractAladdinEntity");
				imports.add("com.aladdin.entities.annotations.AladdinColumn");
				imports.add("com.aladdin.entities.annotations.AladdinDatabaseObject");
				imports.add("com.aladdin.entities.ColumnType");
				imports.add("org.apache.ignite.cache.query.annotations.QuerySqlField");
				imports.add("com.aladdin.ignite.AladdinSybaseCache");
				imports.add("java.io.Externalizable");
				imports.add("java.io.ObjectOutput");
				imports.add("java.io.ObjectInput");
				imports.add("java.io.IOException");
				imports.add("com.aladdin.cloud.Coldboot");
				imports.add("org.apache.commons.lang3.builder.HashCodeBuilder");
				imports.add("org.apache.ignite.cache.CacheAtomicityMode");
				imports.add("org.apache.ignite.cache.CacheMode");
				imports.add("org.apache.ignite.cache.CacheRebalanceMode");
				imports.add("org.apache.ignite.cache.CacheWriteSynchronizationMode");

				for(SybaseTableColumn column : colMap.values())
				{
					if(fields.length() > 0)
					{
						fields.append(DOUBLE_LINE_SPLITTER);
						getters.append(DOUBLE_LINE_SPLITTER);
						equals.append("\n\telse ");
						constructor.append(", ");
						constructorBody.append('\n');
						hashCode.append('\n');
						externalizableOut.append('\n');
						externalizableIn.append('\n');
					}

					SybaseTableIndexInfo key = tableObj.getKey();

					if(key == null)
					{
						LOG.warn("No key for " + table.getKey() + "! Not mapping this table to a POJO!");
						continue;
					}

					String[] defs = ColumnType.getJavaDefinitionFromSybaseSchema(column, key.keys.contains(column.columnName), true, false);

					if(defs == null)
						throw new RuntimeException("Could not map SqlType: " + column.sybaseType + " to java type for table: " + database.name + '.'
								+ tableName + " and column: " + column.columnName);
					else if(defs[0] != null)
						imports.add(defs[0]);

					fields.append(defs[1]);
					constructor.append(defs[2]);
					constructorBody.append(defs[3]);
					getters.append(defs[4]);
					externalizableOut.append(defs[5]);
					externalizableIn.append(defs[6]);
					hashCode.append(defs[7]);
					equals.append(defs[8]);
				}

				StringBuilder sb = new StringBuilder();
				sb.append("package com.aladdin.entities.").append(database.name).append(";\n");

				for(String imp : imports)
					sb.append("\nimport ").append(imp).append(';');

				sb.append("\n\n").append("@AladdinDatabaseObject(getTbl=\"").append(tableName).append("\", keyClass=")
				.append(keyClass).append(".class)\npublic final class ").append(className)
				.append(" extends AbstractAladdinEntity<").append(keyClass).append("> implements Externalizable\n{")
				.append("\n\tprivate static final long serialVersionUID = 1L;\n");

				constructor = new StringBuilder()
				.append("public ").append(className).append('(').append(constructor)
				.append(")\n{\n").append(constructorBody).append("\n}")
				.append(DOUBLE_LINE_SPLITTER).append("public ")
				.append(className).append("() {\n\tsuper(null);\n}\n");

				String cacheMode;
				String writeSynchMode;
				String rebalMode;
				String cacheAtomicityMode;
				int backups;

				if(tableConfig == null)
				{
					cacheMode = "CacheMode.REPLICATED";
					rebalMode = "CacheRebalanceMode.ASYNC";
					writeSynchMode = "CacheWriteSynchronizationMode.FULL_ASYNC";
					cacheAtomicityMode = "CacheAtomicityMode.TRANSACTIONAL";
					backups = 0;
				}
				else
				{
					writeSynchMode = tableConfig.getWriteSynchMode();
					rebalMode = tableConfig.getRebalMode();
					backups = tableConfig.backups;
					cacheAtomicityMode = tableConfig.getCacheAtomicityMode();
					cacheMode = tableConfig.getCacheMode();
				}

				getters.append(DOUBLE_LINE_SPLITTER).append("public static AladdinSybaseCache<")
				.append(keyClass).append(", ").append(className)
				.append("> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)\n{\n\treturn acm.createSybaseCache(name, ")
				.append(keyClass).append(".class").append(", ").append(className).append(".class, coldboot, ").append(writeSynchMode)
				.append(", ").append(rebalMode).append(", ").append(backups).append(", ")
				.append(cacheMode).append(", ").append(cacheAtomicityMode).append(");\n}");

				StringBuilder outAndIn = new StringBuilder();
				outAndIn.append("@Override\npublic void writeExternal(ObjectOutput out) throws IOException\n{\n\tsuper.writeExternal(out);\n")
				.append(externalizableOut)
				.append("\n}")
				.append(DOUBLE_LINE_SPLITTER)
				.append("@Override\npublic void readExternal(ObjectInput in) throws IOException, ClassNotFoundException\n{\n\tsuper.readExternal(in);\n")
				.append(externalizableIn).append("\n}");

				StringBuilder hcb = new StringBuilder()
				.append("@Override\npublic int hashCode()\n{\n\tHashCodeBuilder hcb = new HashCodeBuilder();")
				.append(DOUBLE_LINE_SPLITTER).append(hashCode)
				.append(DOUBLE_LINE_SPLITTER).append("\treturn hcb.toHashCode();\n}");

				StringBuilder eq = new StringBuilder()
				.append("@Override\npublic boolean equals(Object obj)\n{\n")
				.append("\tif(obj == null || !(obj instanceof ").append(className)
				.append("))\n\t\treturn false;").append(DOUBLE_LINE_SPLITTER)
				.append('\t').append(className).append(" o = (").append(className).append(")obj;")
				.append(DOUBLE_LINE_SPLITTER).append(equals).append(DOUBLE_LINE_SPLITTER)
				.append("\treturn true;\n}");

				buildClassBody(sb, fields, constructor, getters, hcb, eq, outAndIn);
				sb.append("\n}");
				File dbFolder = new File(filePath);

				if(!dbFolder.exists())
					dbFolder.mkdirs();

				try(FileWriter writer = new FileWriter(filePath + '/' + className + ".java"))
				{
					writer.write(sb.toString());
				}

				LOG.info("Mapped table: " + tableName);
			}
		}
	}

	private static void buildClassBody(StringBuilder sb, StringBuilder... sections)
	{
		for(StringBuilder section : sections)
		{
			sb.append('\n');

			for(String line : section.toString().split("\n"))
				sb.append('\t').append(line).append('\n');
		}
	}
}