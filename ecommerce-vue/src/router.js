import { createRouter, createWebHistory } from 'vue-router'
import Login from './admin/Login.vue'
const routes = [
  {
    path: '/', // 首页
    name: 'index',
    component: () => import('./before/IndexView.vue'),
    meta: {
      title: '玩偶世界 - 首页'
    }
  },
  {
    path: '/login', // 登录
    name: 'login',
    component: () => import('./before/LoginView.vue'),
    meta: {
      title: '玩偶世界 - 用户登录'
    }
  },
  {
    path: '/register', // 注册
    name:'register',
    component: () => import('./before/RegisterView.vue'),
    meta: {
      title: '玩偶世界 - 用户注册'
    }
  },
  {
    path: '/goodsDetail', // 商品详情
    name: 'goodsDetail',
    component: () => import('./before/GoodsDetailView.vue'),
    meta: {
      title: '玩偶详情'
    }
  },
  {
    path:'/mycart', // 购物车
    name:'mycart',
    component: () => import('./before/MyCartView.vue'),
    meta: {
      title: '我的购物车'
    }
  },
  {
    path: '/goOrder', // 确认订单
    name: 'goOrder',
    component: () => import('./before/GoOrderView.vue'),
    meta: {
      title: '确认订单'
    }
  },
  {
    path: '/myorder', // 我的订单
    name:'myorder',
    component: () => import('./before/MyOrderView.vue'),
    meta: {
      title: '我的订单'
    }
  },
  {
    path: '/myfocus', // 我的收藏
    name:'myfocus',
    component: () => import('./before/MyFocusView.vue'),
    meta: {
      title: '我的收藏'
    }
  },
  {
    path: '/admin/login', // 后台首页
    name: 'admin-login',
    component: Login,
    meta: {
      title: '玩偶世界 - 后台登录'
    }
  },
  {
    path: '/home', // 仪表盘
    name: 'home',
    component: () => import('./admin/Home.vue'),
    meta: {
      title: '玩偶世界 - 后台管理'
    },
    redirect: '/typemanage',
    children: [
      {
        path:'/typemanage',
        name:'typemanage',
        component: () => import('./admin/TypeManage.vue'),
        meta: {
          title: '商品类型'
        }
      },
      {
        path:'/goodsmanage',
        name:'goodsmanage',
        component: () => import('./admin/GoodsManage.vue'),
        meta: {
          title: '管理商品'
        }
      },
      {
        path:'/ordermanage',
        name:'ordermanage',
        component: () => import('./admin/OrderManage.vue'),
        meta: {
          title: '订单管理'
        }
      },
      {
        path:'/usermanage',
        name:'usermanage',
        component: () => import('./admin/UserManage.vue'),
        meta: {
          title: '用户管理'
        }
      },
      {
        path:'/month',
        name:'month',
        component: () => import('./admin/Month.vue'),
        meta: {
          title: '按月统计'
        }
      },
      {
        path:'/orderByMonth',
        name:'orderByMonth',
        component: () => import('./admin/OrderByMonth.vue'),
        meta: {
          title: '按月查询'
        }
      },
      {
        path:'/orderByType',
        name:'orderByType',
        component: () => import('./admin/OrderByType.vue'),
        meta: {
          title: '按类型查询'
        }
      }
    ]
  },
]
const router = createRouter({
  history: createWebHistory(),
  routes
})
export default router