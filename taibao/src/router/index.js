import Vue from 'vue'
import Router from 'vue-router'

import NotFoundComponent from '@/view/error/notFoundCompoment'
import processGo from '@/components/process-go'
import homePage from '@/view/page/news1/homePage'
import api from '@/view/page/news1/api'
import news from '@/view/page/news2/homePage'
import newsApi from '@/view/page/news2/api'

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
      path: '/news',
      name: 'news',
      component: news
    },
    {
      path: '/newsApi',
      name: 'newsApi',
      component: newsApi
    },
    {
      path: '*',
      component: NotFoundComponent
    }
  ]
})

export default router;
