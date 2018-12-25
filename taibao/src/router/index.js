import Vue from 'vue'
import Router from 'vue-router'

import NotFoundComponent from '@/view/error/notFoundCompoment'
import processGo from '@/components/process-go'
import homePage from '@/view/page/news1/homePage'
import api from '@/view/page/news1/api'
import quant from '@/view/page/news2/quant'
/*import quantApi from '@/view/page/news2/quantApi'*/

Vue.use(Router)

const router=new Router({
  mode:"history",
  base:"/taibao/",
  routes: [
    {
      path: '/',
      name: 'homePage',
      component: homePage
    },
    {
      path: '/processGo',
      name: 'processGo',
      component: processGo
    },
    {
      path: '/api',
      name: 'api',
      component: api
    },
    {
      path: '/quant',
      name: 'quant',
      component: quant
    },
 /*   {
      path: '/quantApi',
      name: 'quantApi',
      component: quantApi
    },*/
    {
      path: '*',
      component: NotFoundComponent
    }
  ]
})

export default router;
