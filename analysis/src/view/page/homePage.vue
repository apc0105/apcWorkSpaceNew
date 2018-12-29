<template>
  <div class="main" :style="{height:height+'px'}">
    <div style="padding-top: 60px"><span class="aboutLink">事件分析</span></div>
    <div ref="s_search" class="s_search">
      <div class="logotit">请选择需要上传的文件</div>
      <div class=" srcon clearfix" style="">
        <div ref="serinp" id="serinp" class="searchl">
          <el-input v-model="fileName" readonly="readonly"></el-input>
        </div>
        <div class="searchButton">
          <input type="file" id="file" class="fileButton" value="" @change="onFileChange"/>
          <span class="pcw" style=" position: absolute;top: 0px;left: 28px;font-size: 16px;">上传文件</span>
        </div>

      </div>

      <div ref="serres" class="home_width clearfix" style="display: none;">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane v-for="(item, index) in actives" :key="item.newsID" :label="item.newsName"
                       :name="item.newsID"></el-tab-pane>
        </el-tabs>
        <div class="scommcon" v-for="(item,index) in eventAnalysisList">
          <dl @click="toDetail(item.company,item.code,index,item.star)">
            <dt class="companyName" style="width: 35%;">{{item.company}}（{{item.code}}）</dt>
            <dd v-html="numChangeStar(item.star)"></dd>
          </dl>
          <div class="tableInfo">
            <table :style="{width:item.gaugeNames.length > 6 ?((item.gaugeNames.length+1)*100) +'px':'100%'}">
              <!--数量大于6，加1乘以100px-->
              <tr>
                <td class="tit bd lh_38" v-for="gaugeName in item.gaugeNames">{{gaugeName}}</td>
              </tr>
              <tr>
                <td class="tit" v-for="gaugeValue in item.gaugeValues">{{numFilter(gaugeValue)}}</td>
              </tr>
            </table>
          </div>
          <!--  <dl>
              <dt class="companyName" style="width: 35%;" @click="toDetail(1)">泰康创新中心（123456）</dt>
              <dd v-html="numChangeStar(3)"></dd>
            </dl>
            <div class="tableInfo">
              <table :style="{width:'100%'}">
                &lt;!&ndash;数量大于6，加1乘以100px&ndash;&gt;
                <tr>
                  <td class="tit bd lh_38">历史发生次数</td>
                  <td class="tit bd lh_38">历史平均延后天数</td>
                  <td class="tit bd lh_38">发生波动的概率</td>
                  <td class="tit bd lh_38">发生波动的幅度</td>
                  <td class="tit bd lh_38">1天后平均波动</td>
                  <td class="tit bd lh_38">3天后平均波动</td>
                  &lt;!&ndash;      <td class="tit bd lh_38" >5天后平均波动</td>&ndash;&gt;
                </tr>
                <tr>
                  <td class="tit">10</td>
                  <td class="tit">3</td>
                  <td class="tit">0.34</td>
                  <td class="tit">0.12</td>
                  <td class="tit">0.04</td>
                  <td class="tit">0.12</td>
                  &lt;!&ndash;    <td class="tit" >0.2</td>&ndash;&gt;
                </tr>
              </table>
            </div>-->
        </div>

        <div class="pagebox">
          <el-pagination
            background
            @current-change="handleCurrentChange"
            layout="prev, pager, next"
            :current-page="currentPage"
            :page-count="totalPageNumber">
          </el-pagination>
        </div>
      </div>

    </div>

  </div>

</template>

<script>
  import '@/styles/global.css'
  import '@/styles/homePage.css'
  import {uploadFile, getAnalysisData} from '@/api/analysis'

  export default {
    name: "homePage",
    components: {},
    data() {
      return {
        fileName: '',
        totalPageNumber: 1,
        currentPage: 1,
        pageSize: 4,
        height: '',
        eventAnalysisList: [],
        activeName: '0',
        actives: [],
        keyword: '',
        activeText: ''
      }
    },
    mounted() {
      this.height = window.screen.availHeight - 100;
    },
    methods: {
      numFilter(value) {

        // 截取当前数据到小数点后两位

        let realVal = Number(value).toFixed(3);

        // num.toFixed(2)获取的是字符串

        return Number(realVal)

      },
      handleClick(tab, event) {
        this.activeName = tab.name;
        this.keyword = tab.name;
        this.activeText = tab.label;

        this.openPage(1);
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.openPage(val);
      },
      numChangeStar(num) {
        var starHtml = '';
        var span = '<span></span>';
        var zSpan = '<span class="z"></span>';
        var zbSpan = '<span class="zb"></span>';
        var fSpan = '<span class="f"></span>';
        var fbSpan = '<span class="fb"></span>';

        if ((num < 0.5 && num > 0) || num == 0.0 || num == 0) {
          starHtml = span + span + span + span + span;
        }
        if (num == 0.5) {
          starHtml = zbSpan + span + span + span + span;
        }
        if (num > 0.5 && num < 1.5) {
          starHtml = zSpan + span + span + span + span;
        }
        if (num == 1.5) {
          starHtml = zSpan + zbSpan + span + span + span;
        }
        if (num > 1.5 && num < 2.5) {
          starHtml = zSpan + zSpan + span + span + span;
        }
        if (num == 2.5) {
          starHtml = zSpan + zSpan + zbSpan + span + span;
        }
        if (num > 2.5 && num < 3.5) {
          starHtml = zSpan + zSpan + zSpan + span + span;
        }
        if (num == 3.5) {
          starHtml = zSpan + zSpan + zSpan + zbSpan + span;
        }
        if (num > 3.5 && num < 4.5) {
          starHtml = zSpan + zSpan + zSpan + zSpan + span;
        }
        if (num == 4.5) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zbSpan;
        }
        if (num > 4.5 && num <= 5.0) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zSpan;
        }

        if (num < 0 && num >= -0.5) {
          starHtml = fbSpan + span + span + span + span;
        }
        if (num < -0.5 && num > -1.5) {
          starHtml = fSpan + span + span + span + span;
        }
        if (num == -1.5) {
          starHtml = fSpan + fbSpan + span + span + span;
        }
        if (num < -1.5 && num > -2.5) {
          starHtml = fSpan + fSpan + span + span + span;
        }
        if (num == -2.5) {
          starHtml = fSpan + fSpan + fbSpan + span + span;
        }
        if (num < -2.5 && num > -3.5) {
          starHtml = fSpan + fSpan + fSpan + span + span;
        }
        if (num == -3.5) {
          starHtml = fSpan + fSpan + fSpan + fbSpan + span;
        }
        if (num < -3.5 && num > -4.5) {
          starHtml = fSpan + fSpan + fSpan + fSpan + span;
        }
        if (num == -4.5) {
          starHtml = fSpan + fSpan + fSpan + fSpan + fbSpan;
        }
        if (num < -4.5 && num >= -5.0) {
          starHtml = fSpan + fSpan + fSpan + fSpan + fSpan;
        }

        return starHtml;
      },
      openPage(num) {
        getAnalysisData(num, this.pageSize, this.keyword).then(resp => {
          var respObj = resp.data;

          if (respObj.code == "1") {
            this.eventAnalysisList = respObj.data.eventAnalysisList;
            this.totalPageNumber = respObj.data.total_page;
            this.currentPage = parseInt(respObj.data.current_page);
          }

        })
      },
      onFileChange(e) {
        var files = e.target.files || e.dataTransfer.files || document.getElementById("file").files;
        if (!files.length) return;

        const extension = files[0].name.split('.')[1] == 'txt';
        const extension2 = files[0].name.split('.')[1] == 'TXT';

        if (extension || extension2) {
          this.fileName = files[0].name;
          this.submitFile(files);
        } else {
          alert("上传失败，只能上传txt格式文件");
        }
      },
      submitFile(files) {
        var that = this;
        let formData = new window.FormData()
        formData.append('file', files[0]);
        formData.append('pageSize', this.pageSize);

        uploadFile(formData).then(resp => {

          var respObj = resp.data;

          if (respObj.code == "1") {

            alert("上传成功"); 

            var file = document.getElementById("file");
            file.value = "";

            that.actives = respObj.data.actives;

            if (that.actives == undefined || that.actives.length == 0) {

              setTimeout(() => {
                that.$refs.s_search.style.paddingTop = '100px';
                that.$refs.serres.style.display = 'none';
              }, 200)

              alert("未匹配到数据");

              console.log("数据匹配失败");
              return;
            }
            console.log("map", that.actives);
            console.log("first", that.actives[0]);

            that.activeName = that.actives[0].newsID;
            that.keyword = that.actives[0].newsID;
            that.activeText = that.actives[0].newsName;

            this.openPage(1);

            setTimeout(() => {
              that.$refs.s_search.style.paddingTop = '50px';
              that.$refs.serres.style.display = 'block';
            }, 200)

          } else {
            alert("上传失败");
          }
        }).catch(function (response) {
          console.log(response);
        });
      },
      toDetail(title, code, index, star) {

        let currIdx = (this.currentPage > 1 ? (this.currentPage - 1) * this.pageSize : 0);

        sessionStorage.setItem("title", title);
        sessionStorage.setItem("code", code);
        sessionStorage.setItem("index", index + currIdx);
        sessionStorage.setItem("star", star);
        sessionStorage.setItem("UID", this.activeName);
        sessionStorage.setItem("newstarget", this.activeText);

        const {href} = this.$router.resolve(
          {
            name: 'detail',
          /*  params: {
              title: title,
              code: code,
              index: index + currIdx,
              star: star,
              UID: this.activeName,
              newstarget: this.activeText
            }*/
          });
        window.open(href, '_blank');
      }
    }
  }

</script>
