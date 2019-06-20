# -*- coding: UTF-8 -*-
import MySQLdb
import sys

reload(sys)
sys.setdefaultencoding('utf8')


def getColumnNames(cursor, tableName):
    sql = "select column_name from information_schema.columns where table_schema='mdm' and table_name='%s'" % tableName
    cursor.execute(sql)

    return map(lambda arr: arr[0], cursor.fetchall())


def getUpdateStatement(cursor, tableName):
    statement = 'on duplicate key update '
    primaryKeys = getPrimaryKeyColumns(cursor, tableName)
    columnNames = getColumnNames(cursor, tableName)

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


def getPrimaryKeyColumns(cursor, tableName):
    sql = "SELECT k.column_name FROM information_schema.table_constraints t \
            JOIN information_schema.key_column_usage k \
            USING (constraint_name,table_schema,table_name) \
            WHERE t.constraint_type='PRIMARY KEY' \
            AND t.table_schema='mdm' \
            AND t.table_name='%s'" % tableName

    cursor.execute(sql)

    return map(lambda row: row[0], cursor.fetchall())


# 生成 删除正式环境数据的sql
def getDeleteSqlStatement(cursor, tableName):
    sql = "delete from %s where " % tableName
    primaryKeys = getPrimaryKeyColumns(cursor, tableName)

    for primaryKey in primaryKeys:
        if not sql.endswith("where "):
            sql += " and "

        sql += primaryKey + " = \"%s\""

    return sql + ";"


# 生成 获取试运行环境删除的数据的sql
def getQueryDeletedDataSqlStatement(cursor, tableName):
    sql = "select primaryKeys from mdm.%s prod left join mdm_pilot.%s pilot on " % (tableName, tableName)
    primaryKeys = getPrimaryKeyColumns(cursor, tableName)

    for primaryKey in primaryKeys:
        if not sql.endswith("on "):
            sql += " and "

        sql += "prod.%s = pilot.%s" % (primaryKey, primaryKey)

    sql = (sql + " where pilot.%s is null" % primaryKeys[0]).replace("primaryKeys", ",".join(
        map(lambda column: "prod." + column, primaryKeys)))
    return sql


db = MySQLdb.connect("10.1.22.28", "xkw", "xkw.com1QAZ", "mdm", charset='utf8')
cursor = db.cursor()


# print getDeleteSqlStatement(cursor, "similar_catalog_group")
# print getQueryDeletedDataSqlStatement(cursor, "similar_catalog_group")
#
# a = ("12", "b")
# print "12" in a
