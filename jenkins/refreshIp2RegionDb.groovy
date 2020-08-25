node(params.NODE_NAME) {
    stage('download ip2region db') {
//        sh 'wget -O /data/wangwei/ip2region.db https://gitee.com/lionsoul/ip2region/raw/master/data/ip2region.db'

        git 'https://gitee.com/lionsoul/ip2region.git'
    }
}