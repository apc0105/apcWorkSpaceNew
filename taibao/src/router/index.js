import Vue from 'vue'
import Router from 'vue-router'
import homePage from '@/view/page/homePage'
import NotFoundComponent from '@/view/error/notFoundCompoment'
import processGo from '@/components/process-go'

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
      path: '*',
      component: NotFoundComponent
    }
  ]
})

export default router;
