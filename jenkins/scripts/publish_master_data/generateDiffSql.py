# -*- coding: UTF-8 -*-
import codecs
import MySQLdb
import sys
import dbUtils

tables = ("similar_catalog_group",)
# tables = ("knowledge_points",
#           # "similar_catalog_group", "tcatalog_kpoint",
#           "textbook_attachment","textbook_catalogs", "textbook_versions", "textbooks", "version_families", "kpoint_cards",
#           "exam_areas", "exam_subjects", "exam_area_subject", "tricks", "trick_cards")

diffSqlFile = sys.argv[1]
file = codecs.open(diffSqlFile, "w+", "utf-8")

db = MySQLdb.connect("10.1.22.28", "xkw", "xkw.com1QAZ", "mdm", charset='utf8')
cursor = db.cursor()

def writeUtf8( content):
    global file
    file.write(content.decode("utf-8"))
    file.write("\n")

# 生成delete diff sql
def generateDeleteSql(tableName):
    global cursor

    deleteSqlTemplate = dbUtils.getDeleteSqlStatement(cursor, tableName)

    sql = dbUtils.getQueryDeletedDataSqlStatement(cursor, tableName)

    cursor.execute(sql)

    results = cursor.fetchall()
    if  len(results) <= 0:
        return


    ids = map(lambda row: str(row[0]).replace("\"", "\\\""), results)
    for idValues in results:
        idValues = map(lambda id: str(id).replace("\"", "\\\""), idValues)

        # print deleteSqlTemplate % tuple(idValues)
        writeUtf8(deleteSqlTemplate % tuple(idValues))

    # print results

# 生成diff sql
def generateDiffSql(tableName):
    # 先生成delete的sql
    generateDeleteSql(tableName)
    global cursor
    # 获取列名
    allColumns = dbUtils.getColumnNames(cursor, tableName)
    # 获取update从句
    updateStatement = dbUtils.getUpdateStatement(cursor, tableName)
    columnList = ','.join(allColumns)
    sqlPart1 = "insert into %s(%s) value " % (tableName, columnList)
    # SQL 查询语句
    sql = "select * from %s" % tableName

    cursor = db.cursor()
    # 执行SQL语句
    cursor.execute(sql)
    # 获取所有记录列表
    results = cursor.fetchall()
    # print results

    for row in results:
        valueList = ",".join(map(lambda x: "\"%s\"" % str(x).replace("\"", "\\\""), row))
        # print "%s(%s)" % (sqlPart1, valueList)

        # print "%s(%s) %s" % (sqlPart1, valueList, updateStatement)
        writeUtf8("%s(%s) %s" % (sqlPart1, valueList, updateStatement))

writeUtf8("use mdm;")
for table in tables:
    writeUtf8("# %s的diff sql >>>>>>>>>>>>>>>>>>>>>>>>> start\n" % table)
    generateDiffSql(table)
    writeUtf8("# 以上是%s的diff sql <<<<<<<<<<<<<<<<<<<< end\n" % table)
    # print '\n'

file.close()

db.close()

