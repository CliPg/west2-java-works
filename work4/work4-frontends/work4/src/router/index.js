import { createRouter, createWebHistory } from 'vue-router';
import User from '@/views/User.vue';
import HomeView from '@/views/HomeView.vue';
import store from "@/store";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  // {
  //   path: '/user',
  //   name: 'User',
  //   component: User
  // },
  {
    path: '/user',
    name: 'User',

    component: () => import(/* webpackChunkName: "about" */ '../views/User.vue'),
    children:[
      {
        path:"login",
        component:() => import("../components/User/Login.vue")
      },
      {
        path:"register",
        component:() => import("../components/User/Register.vue")
      },
      {
        path:"logout",
        component:() => import("../components/User/Logout.vue"),
        beforeEnter: (to, from, next) => {
          console.log('检查用户登录状态:', store.state.user.token);
          // 检查 token 是否存在
          if (store.state.user.token) {
            console.log('用户已登录');
            next(); // 用户已登录，继续访问
          } else {
            next('/user/login'); // 未登录，重定向到登录页面
          }
        }

      },
      {
        path:"avatar/upload",
        component:() => import("../components/User/AvatarUpload.vue"),
        beforeEnter: (to, from, next) => {
          // 检查 token 是否存在
          if (store.state.user.token) {
            console.log('用户已登录');
            next(); // 用户已登录，继续访问
          } else {
            next('/user/login'); // 未登录，重定向到登录页面
          }
        }
      },

    ]
  },
  {
    path: '/video',
    name: 'Video',

    component: () => import(/* webpackChunkName: "about" */ '../views/Video.vue'),
    children:[
      {
        path:"publish",
        component:() => import("../components/Video/PublishVideo.vue"),
        beforeEnter: (to, from, next) => {
          // 检查 token 是否存在
          if (store.state.user.token) {
            console.log('用户已登录');
            next(); // 用户已登录，继续访问
          } else {
            next('/user/login'); // 未登录，重定向到登录页面
          }
        }
      },
      {
        path:"popular",
        component:() => import("../components/Video/Popular.vue"),
      },
      {
        path:"search",
        component:() => import("../components/Video/Search.vue"),
      },

    ]
  },
  {
    path: '/interaction',
    name: 'Interaction',
    component:() => import("../views/Interaction.vue"),
  },
  {
    path: '/social',
    name: 'Social',
    component:() => import("../views/Social.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;



