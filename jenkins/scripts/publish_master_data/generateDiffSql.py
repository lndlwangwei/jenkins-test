# -*- coding: UTF-8 -*-
import codecs
import dbUtils
import MySQLdb
import sys

tables = ("knowledge_points","similar_catalog_group", "tcatalog_kpoint","textbook_attachment",
          "textbook_catalogs", "textbook_versions", "textbooks", "version_families", "kpoint_cards",
          "exam_areas", "exam_subjects", "exam_area_subject", "tricks", "trick_cards")
diffSqlFile = sys.argv[1]

db = MySQLdb.connect("10.1.22.28", "xkw", "xkw.com1QAZ", "mdm", charset='utf8')
cursor = db.cursor()

updateStatements = {}

for table in tables:
    updateStatements[table] = " " + dbUtils.getUpdateStatement(table)

print updateStatements

file = codecs.open(diffSqlFile, "r", "utf-8")
lines = file.readlines()
file.close()
file = codecs.open(diffSqlFile, "a", "utf-8")

def writeUtf8( content):
    global file
    file.write(content.decode("utf-8"))
    file.write("\n")

# 生成delete diff sql
def appendDeleteSql():
    global cursor

    for tableName in tables:

        deleteSqlTemplate = dbUtils.getDeleteSqlStatement(tableName)

        sql = dbUtils.getQueryDeletedDataSqlStatement(tableName)

        cursor.execute(sql)

        results = cursor.fetchall()
        if  len(results) <= 0:
            continue


        for idValues in results:
            idValues = map(lambda id: resolveColumnValue(id), idValues)

            # print deleteSqlTemplate % tuple(idValues)
            writeUtf8(deleteSqlTemplate % tuple(idValues))

    # print results

# 处理字段值
def resolveColumnValue(value):
    if value == None:
        return 'null'
    elif isinstance(value, (int, long, float)):
        return str(value)
    else:
        return "\'%s\'" % str(value).replace("\'", "\\\'")

# pattern = re.compile(u"^INSERT INTO `(.+?)`.+$")
#
# for line in lines:
#     matchObject = pattern.match(line)
#     if not matchObject:
#         file.write(line)
#         continue
#
#     tableName = matchObject.groups()[0]
#     print tableName
#     line = re.sub(";$", " " + updateStatements[tableName], line)
#     file.write(line)

appendDeleteSql()

file.close()
