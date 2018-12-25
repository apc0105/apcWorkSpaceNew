<template>
  <div class="main" :style="{height:height+'px'}" v-myOn:click="fn">
    <div><span class="aboutLink" @click="openApi">资源下载</span></div>
    <div ref="s_search" class="s_search">
      <div class="logotit"><img src="../../../assets/images/img_wenzi.png"/><span class="newWord">（量化）</span></div>
      <div class=" srcon clearfix" style="">
        <div ref="serinp" id="serinp" class="searchl" @click="handleshow($event)">
          <el-input v-model="shiftName" @focus="handleinput($event)" @input.native="handleinput($event),search(1)"
                    ><!--@keyup.enter.native="enter($event)"
                     onkeyup="this.value=this.value.replace(/[^\u4E00-\u9FA5]/g,'');"-->
            <!-- <el-select v-model="nDirectionSelect" slot="prepend" placeholder="请选择"
                        style="width: 80px;background-color: #435a6c;">
                <el-option label="上游" value="0"></el-option>
               <el-option label="下游" value="0"></el-option>
             </el-select>-->
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
        <div class="searchButton" @click="openForm">
          泰宝一下
        </div>
      </div>

      <div ref="serres" class="home_width1 clearfix" style="display: none;">
        <div class="scomm">泰宝为您找到受影响公司结果约 <em>{{totalNum}}</em> 个。<span class="z">正面影响力</span><span
          class="f">负面影响力</span></div>
        <div class="scommcon1" style="border-right: 10px solid #f8f8f8;">
          <dl>
            <dt class="tit">股票名称</dt>
            <dd class="tit">影响程度及方向</dd>
          </dl>

          <dl v-for="item in smartSearchList">
            <dt @click="showDialog(item)">{{item.name}}（{{item.code}}）</dt>
            <dd v-html="numChangeStar(item.score)"></dd>
          </dl>

          <div class="pagebox" style="padding-left: 10px;">

            <el-pagination
              background
              @current-change="handleCurrentChange"
              layout="prev, pager, next"
              :current-page="currentPage"
              :page-count="totalPageNumber">
            </el-pagination>
          </div>
        </div>
        <div class="scommcon1">
          <dl>
            <dt class="tit">股票名称</dt>
            <dd class="tit">影响程度及方向</dd>
          </dl>

          <dl v-for="item in fsmartSearchList">
            <dt @click="showDialog(item)">{{item.name}}（{{item.code}}）</dt>
            <dd v-html="numChangeStar(item.score)"></dd>
          </dl>

          <div class="pagebox" style="padding-left: 10px;">

            <el-pagination
              background
              @current-change="fhandleCurrentChange"
              layout="prev, pager, next"
              :current-page="fcurrentPage"
              :page-count="ftotalPageNumber">
            </el-pagination>
          </div>
        </div>
      </div>

    </div>

    <el-dialog :title="record.name" v-if="dialogTableVisible" :visible.sync="dialogTableVisible" width="90%"
               @close="closeDialog">
      <process-go v-bind:record="record" ref="processGoPage"></process-go>
    </el-dialog>

    <el-dialog title="" :visible.sync="dialogFormVisible" align="center" width="416px" :style="{marginTop:'150px'}">
      <span style="font-size: 14px;color: #042E58;letter-spacing: 0;">原料价格变动比例</span>
      <el-input v-model="flPriceChng" style="width: 100px;height: 30px;" type="number" :step="0.01"></el-input>
      (%)
      <div slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button @click="dialogFormVisible = false"
                   style="width:80px;height:30px;border: 1px solid #042E58;font-size: 14px;color: #042E58;padding: 0;border-radius: 2px;">
          取 消
        </el-button>
        <el-button type="primary" @click="search(0)"
                   style="width:80px;height:30px;border: 1px solid #042E58;background:#042E58;font-size: 14px;color: #FFFFFF;padding: 0;border-radius: 2px;">
          确 定
        </el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
  import '@/styles/homePage.css'
  import {smartSearch, smartSearchPage, smartSearchEdge, searchHintKeys} from '@/api/homePage'
  import processGo from '@/components/process-go'
  import NProgress from 'nprogress'

  export default {
    name: "quantHomePage",
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
        dialogFormVisible: false,
        smartSearchList: [],
        fsmartSearchList: [],
        totalPageNumber: 1,
        ftotalPageNumber: 1,
        currentPage: 1,
        fcurrentPage: 1,
        totalNum: 0,
        token: '',
        record: {
          name: '',
          code: ''
        },
        nDirectionSelect: '0',
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
          el.addEventListener(event, fn);
        }
      }
    },
    mounted() {
      // this.height = window.screen.availHeight - 100;
      // this.height = document.documentElement.clientHeight;
      this.height = window.innerHeight
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
          }else {
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
      openForm() {
        this.dialogFormVisible = true;
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.openPage('z');
      },
      fhandleCurrentChange(val) {
        this.fcurrentPage = val;
        this.openPage('f');
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
            that.$refs.serres.className = "home_width1 clearfix";

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
          this.$refs.serres.className = "home_width1 clearfix s_ani_sh0";
          this.totalNum = 0;
        }
        this.dialogFormVisible = false;
      },
      handleinput(e) {
        var that = this;
        var el = e.target;
        if (el.innerText != "" && el.innerText != undefined) {
          that.$refs.sermess.style.display = 'none';
        } else {
            that.shiftName = el.value;
            that.initSearchKeys(el.value,function () {
              that.$refs.sermess.style.display = 'block';
            });
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
      /* enter(e) {
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

      },
      closeDialog() {
        this.dialogTableVisible = false;
      },
      initPage(searchValue, callback) {
        smartSearch(this.pageSize, this.nDirectionSelect, searchValue, (this.flPriceChng / 100)).then(resp => {
          let respObj = resp.data;
          if (respObj.code != 1) {
            NProgress.done();
            alert("未查询到数据！");
            return;
          }
          console.log("resp", resp)
          this.smartSearchList = respObj.data.nodes;
          this.fsmartSearchList = respObj.data.fnodes;
          this.totalPageNumber = respObj.data.total_page;
          this.ftotalPageNumber = respObj.data.ftotal_page;
          this.currentPage = respObj.data.current_page;
          this.fcurrentPage = respObj.data.fcurrent_page;

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
        var starHtml = '';
        var span = '<span></span>';
        var zSpan = '<span class="z"></span>';
        var zbSpan = '<span class="zb"></span>';
        var fSpan = '<span class="f"></span>';
        var fbSpan = '<span class="fb"></span>';

        if (num==0) {
          starHtml = span + span + span + span + span;
        }
        if (num == 0.5) {
          starHtml = zbSpan + span + span + span + span;
        }
        if (num == 1) {
          starHtml = zSpan + span + span + span + span;
        }
        if (num == 1.5) {
          starHtml = zSpan + zbSpan + span + span + span;
        }
        if (num == 2) {
          starHtml = zSpan + zSpan + span + span + span;
        }
        if (num == 2.5) {
          starHtml = zSpan + zSpan + zbSpan + span + span;
        }
        if (num == 3) {
          starHtml = zSpan + zSpan + zSpan + span + span;
        }
        if (num == 3.5) {
          starHtml = zSpan + zSpan + zSpan + zbSpan + span;
        }
        if (num == 4) {
          starHtml = zSpan + zSpan + zSpan + zSpan + span;
        }
        if (num == 4.5) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zbSpan;
        }
        if (num == 5) {
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
        if (num <-3.5 && num > -4.5) {
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
      openPage(zf) {
        smartSearchPage(this.pageSize, this.nDirectionSelect, this.currentPage, this.fcurrentPage, this.token, this.flPriceChng).then(resp => {
          let respObj = resp.data;
          if (zf =="z") {
            this.smartSearchList = respObj.data.nodes;
            this.totalPageNumber = respObj.data.total_page;
            this.currentPage = parseInt(respObj.data.current_page);
          }
          if (zf =="f") {
            this.fsmartSearchList = respObj.data.fnodes;
            this.ftotalPageNumber = respObj.data.ftotal_page;
            this.fcurrentPage = parseInt(respObj.data.fcurrent_page);
          }
        })
      },
      openApi() {
        this.$router.push('/quantApi');
      }
    }
  }

</script>
