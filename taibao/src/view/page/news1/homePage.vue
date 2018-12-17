<template>
  <div class="main" :style="{height:height+'px'}" v-myOn:click="fn">
    <div><span class="aboutLink" @click="openApi()">资源下载</span></div>
    <div ref="s_search" class="s_search">
      <div class="logotit"><img src="../../../assets/images/img_wenzi.png"/></div>
      <div class=" srcon clearfix" style="">
        <div ref="serinp" id="serinp" class="searchl" @click="handleshow($event)">
          <el-input v-model="shiftName" @focus="handleInput($event)" @input.native="handleInput($event),search(1)">
            <!--@keyup.enter.native="enter($event)" <!--@input.native="handleinput($event),search(1)"-
            onkeyup="this.value=this.value.replace(/[^\u4E00-\u9FA5]/g,'');"->-->
            <el-select v-model="nDirectionSelect" slot="prepend" placeholder="请选择"
                       style="width: 80px;background-color: #435a6c;">
              <el-option label="上游" value="1"></el-option>
              <el-option label="下游" value="0"></el-option>
            </el-select>
          </el-input>
          <!--<div class="sod"></div>-->
          <div class="sc" id="sermess" ref="sermess">
            <ul>
              <li style="border-bottom:#ffffff solid 1px;" v-for="(item,index) in keyWordsList"
                  @click="inputMess($event)" :key="index">
                {{ item.name }}
              </li>
            </ul>
            <!--      <div class="cz">
                    <div class="close" @click="closeSerMess($event)">取消</div>
                    <div class="del" @click="closeSerMess($event)">清除记录</div>
                  </div>-->
          </div>
        </div>
        <div class="searchButton" @click="search(0)">
          泰宝一下
        </div>
      </div>

      <div ref="serres" class="home_width clearfix" style="display: none;">
        <div class="scomm">泰宝为您找到受影响公司结果约 <em>{{totalNum}}</em> 个。<span class="z">正面影响力</span><span
          class="f">负面影响力</span></div>
        <div class="scommcon">
          <dl>
            <dt class="tit">股票名称</dt>
            <dd class="tit">影响程度及方向</dd>
          </dl>

          <dl v-for="item in smartSearchList">
            <dt @click="showDialog(item)">{{item.name}}（{{item.code}}）</dt>
            <dd v-html="numChangeStar(item.score)"></dd>
          </dl>

          <div class="pagebox">
            <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              layout="prev, pager, next"
              :page-count="totalPageNumber">
            </el-pagination>
          </div>
        </div>

      </div>

    </div>

    <el-dialog :title="record.name" v-if="dialogTableVisible" :visible.sync="dialogTableVisible" width="90%"
               @close="closeDialog">
      <process-go v-bind:record="record" ref="processGoPage"></process-go>
    </el-dialog>

  </div>

</template>

<script>
  import '@/styles/homePage.css'
  import {smartSearch, smartSearchPage, smartSearchEdge, searchHintKeys} from '@/api/homePage'
  import processGo from '@/components/process-go'
  import NProgress from 'nprogress'

  export default {
    name: "homePage",
    components: {
      processGo
    },
    data() {
      return {
        shiftName: '',
        keyWordsList: [],
        historicalRecordsList: [],
        historicalRecordsListSize: 20,
        dialogTableVisible: false,
        searchResultVisible: false,
        smartSearchList: [],
        totalPageNumber: 1,
        currentPage: 1,
        totalNum: 0,
        token: '',
        record: {
          name: '',
          code: ''
        },
        nDirectionSelect: '1',
        height: '',
        flPriceChng: '',
        pageSize: 10
      }
    },
    directives: {
      myOn: {
        bind(el, binding, vnode) {
          const event = binding.arg;
          const fn = binding.value;
          console.log(el);
          el.addEventListener(event, fn);
        }
      }
    },
    mounted() {
      this.height = window.innerHeight;
      this.initSearchKeys("",function () {
      });
    },
    methods: {
      initSearchKeys(keyWords,callback) {

        searchHintKeys(keyWords).then(resp => {
          let respObj = resp.data;
          if (respObj.code != 1) {
            console.log("未查询到数据！");
            return;
          }
          this.keyWordsList = respObj.data;

          if (keyWords == "") {
            this.keyWordsList = this.historicalRecordsList.concat(
              this.keyWordsList
            );
          }

          if (this.keyWordsList.length > 5) {
            this.$refs.sermess.style.height = "160px";
            this.$refs.sermess.style.overflow = "auto";
          }

          if (this.keyWordsList.length == 0) {
            this.$refs.sermess.style.display = 'none';
          }else{
            callback();
          }

        })
      },
      fn() {
        document.getElementById('sermess').style.display = "none";
        var si = document.getElementById('serinp');
        if (si.firstElementChild.firstElementChild.value == "") {
          si.className = "searchl"
        } else {
          si.className = "searchl focus"
        }
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.openPage(val);
      },
      search(isFoc) {
        var that = this;

        if ((/^[\u4e00-\u9fa5]/g.test(this.shiftName) || this.shiftName.length > 0) && isFoc == 0) {
          NProgress.inc(0.2);
          NProgress.configure({easing: 'ease', speed: 500});
          NProgress.start();

          this.initPage(this.shiftName, function () {
            that.$refs.sermess.style.display = 'none';
            that.$refs.s_search.className = "s_search s_ani_d";
            that.$refs.serres.className = "home_width clearfix";

            setTimeout(() => {
              NProgress.done();
              that.$refs.serres.style.display = 'block';
            }, 200)

            that.height = window.innerHeight;
            if (that.height < 660) {
              that.height = that.height + 200;
            }
            if (that.height < 760) {
              that.height = that.height + 100;
            }
          });
        } else {
          this.height = window.innerHeight;
          this.$refs.serres.style.display = 'none';
          this.$refs.s_search.className = "s_search s_ani_h";
          this.$refs.serres.className = "home_width clearfix s_ani_sh0";
          this.totalNum = 0;
        }

      },
      handleInput(e) {
        var that = this;
        var el = e.target;

        if (el.innerText != "" && el.innerText != undefined) {
          that.$refs.sermess.style.display = 'none';
        } else {
          if (/^[\u4e00-\u9fa5]/g.test(el.value)) {
            that.shiftName = el.value;
            that.initSearchKeys(el.value,function () {
              that.$refs.sermess.style.display = 'block';
            });
          }
        }

      },
      closeSerMess(e) {
        e.stopPropagation();
        e.cancelBubble = true;
        this.$refs.sermess.style.display = 'none';
      },
      inputMess(e) {
        var el = e.target;
        e.stopPropagation();
        e.cancelBubble = true;
        this.shiftName = el.innerText;
        this.$refs.sermess.style.display = 'none';
        this.$refs.serinp.className = "searchl focus";
      },
      /*  enter(e) {
          console.log("enter");
          var el = e.target.value;
          this.$refs.sermess.style.display = 'none';
          this.search(0);
        },*/
      handleshow(e) {
        e.stopPropagation();
        e.cancelBubble = true;
        this.$refs.sermess.style.display = 'block';
      },
      showDialog(item) {
        this.record.name = item.name;
        this.record.code = item.code;
        this.dialogTableVisible = true;
        /*var that = this;
        setTimeout(() => {
          that.$refs.processGoPage.initData(function () {
            this.$refs.processGoPage.load();
          });
        }, 200)*/

      },
      closeDialog() {
        this.dialogTableVisible = false;
      },
      initPage(searchValue, callback) {
        var that = this;
        smartSearch(this.pageSize, this.nDirectionSelect, searchValue, 0).then(resp => {
          let respObj = resp.data;
          if (respObj.code != 1) {
            NProgress.done();
            alert("未查询到数据！");
            return;
          }
          console.log("resp", resp)
          this.smartSearchList = respObj.data.nodes;
          this.totalPageNumber = respObj.data.total_page;

          this.currentPage = respObj.data.current_page;

          if (respObj.data.total_num != null) {
            this.totalNum = respObj.data.total_num;
            console.log("height", this.$refs.sermess);

            this.dealKeyWordsAndHistoricalRecord(searchValue);

          }
          this.token = respObj.data.token;
          sessionStorage.clear();
          sessionStorage.setItem('token', this.token);
          callback();
        }).catch(function (response) {
          NProgress.done();
        })
      },
      dealKeyWordsAndHistoricalRecord(searchValue) {
        let keyWordFlag = false;
        let historyWordFlag = false;

        if (this.keyWordsList != null && this.keyWordsList.length > 0) {
          for (let i = 0; i < this.keyWordsList.length; i++) {
            let keyWord = this.keyWordsList[i].name;
            if (keyWord == searchValue) {
              keyWordFlag = true;
              break;
            }
          }
        }

        if (this.historicalRecordsList != null && this.historicalRecordsList.length > 0) {
          for (let j = 0; j < this.historicalRecordsList.length; j++) {
            let keyWord = this.historicalRecordsList[j].name;
            if (keyWord == searchValue) {
              historyWordFlag = true;
              break;
            }
          }
        }

        if (!keyWordFlag && !historyWordFlag) {

          let historicalRecord = {"name": searchValue};
          //向历史记录中添加数据
          this.historicalRecordsList.splice(0, 0, historicalRecord);
          this.$refs.sermess.style.height = "160px";
          this.$refs.sermess.style.overflow = "auto";
          if (this.historicalRecordsList.length + this.keyWordsList.length > this.historicalRecordsListSize) {
            this.historicalRecordsList.pop();
          }
        }
      },
      numChangeStar(num) {
        num = num * 5;
        var starHtml = '';
        var span = '<span></span>';
        var zSpan = '<span class="z"></span>';
        var zbSpan = '<span class="zb"></span>';
        var fSpan = '<span class="f"></span>';
        var fbSpan = '<span class="fb"></span>';

        if (num < 0.5 && num >= 0) {
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
        smartSearchPage(this.pageSize, this.nDirectionSelect, num, 0, this.token, 0).then(resp => {
          let respObj = resp.data;
          this.smartSearchList = respObj.data.nodes;
          this.totalPageNumber = respObj.data.total_page;
          this.currentPage = parseInt(respObj.data.current_page);
        })
      },
      openApi() {
        this.$router.push('/api');
      }
    }
  }

</script>
