<template>
  <div class="home">
    <div class="left" :style="{height:quantHeight+'px'}">
      <div class="type">
        <div class="document">
          <label>文档类型</label>
        </div>
      </div>

      <div class="list">
        <ul>
          <li v-for="item in quantPackageTypes" @click="getQuantApiInfo(item)">{{item}}</li>
        </ul>
      </div>
    </div>

    <div class="right">
      <div class="h">{{quantPackageType}}</div>
      <div class="table">
        <table>
          <thead>
          <tr>
            <th>包</th>
            <th>版本号</th>
            <th>API地址</th>
          </tr>
          </thead>
          <tr v-for="item in quantMainPackages" >
            <th class="thbb"><a :href="quantBaseUrl+item.packageRelativeAddress" class="fc down">{{item.packageName}}</a></th>
            <th class="thbb">{{item.packageVersion}}</th>
            <th class="thbb"><a :href="quantBaseUrl+item.docRelativeAddress" class="fc">{{item.docName}}</a></th>
          </tr>
          <tr v-if="quantDependencyPackages.length >0">
            <th colspan="3" class="bot">
              <ul>
                <li>依赖包</li>
                <li v-for="item in quantDependencyPackages"><a :href="quantBaseUrl+item.packageRelativeAddress" class="fc down">{{item.packageName}}</a>
                </li>
              </ul>
            </th>
          </tr>
        </table>
        <div style="margin-top: 20px;">
          <el-button class="download" @click="quantHandleBatchDownload" style="background: #042E58;font-size: 14px;color: #FFFFFF;"><i class="el-icon-download"></i>&nbsp;批量下载</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import '@/styles/api.css'
  import {getPackageTypes, getApiInfos} from '@/api/api'

  import axios from 'axios'
  import JSZip from 'jszip'
  import FileSaver from 'file-saver'

  //获取文件
  const getFile = url => {

    return new Promise((resolve, reject) => {

      axios({

        method: 'get',

        url,

        responseType: 'arraybuffer'

      }).then(data => {

        resolve(data.data)

      }).catch(error => {

        reject(error.toString())

      })

    })

  }

  export default {
    name: "api",
    data() {
      return {
        quantHeight: '',
        quantPackageTypes: [],
        quantPackageType: '',
        quantMainPackages: [],
        quantDependencyPackages: [],
        quantBaseUrl: this.baseUrl
      }
    },
    mounted() {
      this.initSetting();
      this.initPackageType();
    },
    methods: {
      initSetting() {
        this.quantHeight = window.innerHeight;
        console.log('quantHeight', this.quantHeight);
      },
      initPackageType() {
        getPackageTypes().then(resp => {
          let respObj = resp.data;
          if (respObj.code != 1) {
            alert("未查询到数据！");
            return;
          }
          //初始化类型
          this.quantPackageTypes = respObj.data;
          //初始化第一个类型下数据
          this.getQuantApiInfo(this.quantPackageTypes[0]);
        })
      },
      getQuantApiInfo(packageType) {
        this.quantPackageType = packageType;

        getApiInfos(packageType).then(resp => {
          let respObj = resp.data;
          if (respObj.code != 1) {
            alert("未查询到数据！");
            return;
          }
          this.quantMainPackages = respObj.data.mainPackages;
          if (respObj.data.dependencyPackages.length > 0) {
            this.quantDependencyPackages = respObj.data.dependencyPackages;
          }
        })
      },
      quantHandleBatchDownload() {
        const aList =  document.getElementsByClassName("down");
        const data = [] // 需要下载打包的路径, 可以是本地相对路径, 也可以是跨域的全路径
        console.log("alist", aList)
        for (var i = 0; i < aList.length; i++) {
          data.push(aList[i].href)
        }
        console.log("data", data)
        const zip = new JSZip()

        const cache = {}

        const promises = []
        data.forEach(item => {

          const promise = getFile(item).then(data => { // 下载文件, 并存成ArrayBuffer对象

            const arr_name = item.split("/")

            const file_name = arr_name[arr_name.length - 1] // 获取文件名

            zip.file(file_name, data, {binary: true}) // 逐个添加文件

            cache[file_name] = data

          })

          promises.push(promise)

        })

        Promise.all(promises).then(() => {

          zip.generateAsync({type: "blob"}).then(content => { // 生成二进制流

            FileSaver.saveAs(content, this.quantPackageType + "打包下载.zip") // 利用file-saver保存文件

          })

        })

      }
    }
  }

</script>
