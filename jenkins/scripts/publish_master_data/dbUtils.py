# -*- coding: UTF-8 -*-
import sys
import MySQLdb

reload(sys)
sys.setdefaultencoding('utf8')

db = MySQLdb.connect("10.1.22.28", "xkw", "xkw.com1QAZ", "mdm", charset='utf8')
cursor = db.cursor()

def getColumnNames(tableName):
    global cursor
    sql = "select column_name from information_schema.columns where table_schema='mdm' and table_name='%s'" % tableName
    cursor.execute(sql)

    return map(lambda arr: arr[0], cursor.fetchall())

def getQueryFields(tableName):
    global cursor
    sql = "select column_name, column_type from information_schema.columns where table_schema='mdm' and table_name='%s'" % tableName
    cursor.execute(sql)

    return ",".join(map(lambda row: convertBitToInt(row[0], row[1]), cursor.fetchall()))


def getUpdateStatement(tableName):
    global cursor
    statement = 'on duplicate key update '
    primaryKeys = getPrimaryKeyColumns(tableName)
    columnNames = getColumnNames(tableName)

    for i in range(0, len(columnNames)):
        columnName = columnNames[i]

        if columnName in primaryKeys:
            continue
        if not statement.endswith("update "):
            statement += ','

        # print value
        statement += "%s = values(`%s`)" % (columnName, columnName)

    # print statement
    return statement + ";"

# 获取主键列
def getPrimaryKeyColumns(tableName):
    global cursor
    sql = "SELECT k.column_name FROM information_schema.table_constraints t \
            JOIN information_schema.key_column_usage k \
            USING (constraint_name,table_schema,table_name) \
            WHERE t.constraint_type='PRIMARY KEY' \
            AND t.table_schema='mdm' \
            AND t.table_name='%s'" % tableName

    cursor.execute(sql)

    return map(lambda row: row[0], cursor.fetchall())


# 生成 删除正式环境数据的sql
def getDeleteSqlStatement(tableName):
    global cursor
    sql = "delete from %s where " % tableName
    primaryKeys = getPrimaryKeyColumns(tableName)

    for primaryKey in primaryKeys:
        if not sql.endswith("where "):
            sql += " and "

        sql += primaryKey + " = %s"

    return sql + ";"


# 生成 获取试运行环境删除的数据的sql
def getQueryDeletedDataSqlStatement(tableName):
    global cursor
    sql = "select primaryKeys from mdm.%s prod left join mdm_pilot.%s pilot on " % (tableName, tableName)
    primaryKeys = getPrimaryKeyColumns(tableName)

    for primaryKey in primaryKeys:
        if not sql.endswith("on "):
            sql += " and "

        sql += "prod.%s = pilot.%s" % (primaryKey, primaryKey)

    sql = (sql + " where pilot.%s is null" % primaryKeys[0]).replace("primaryKeys", ",".join(
        map(lambda column: "prod." + column, primaryKeys)))
    return sql

def convertBitToInt(columnName, type):
    if  type.startswith('bit'):
        return "convert(%s, %s)" % (columnName, 'unsigned')
    else:
        return columnName

# db = MySQLdb.connect("10.1.22.28", "xkw", "xkw.com1QAZ", "mdm", charset='utf8')
# cursor = db.cursor()
# 
# print getQueryFields('tricks')