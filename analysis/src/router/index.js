import Vue from 'vue'
import Router from 'vue-router'
import homePage from '@/view/page/homePage'
import detail from '@/view/page/detail'

Vue.use(Router)

const router = new Router({
  mode:"history",
  base:"/analysis/",
  routes: [
    {
      path: '/',
      name: 'homePage',
      component: homePage
    },
    {
      path: '/detail',
      name: 'detail',
      component: detail
    }
  ]
})

export default router
