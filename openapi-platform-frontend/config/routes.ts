export default [
  {
    path: '/home',
    name: '首页',
    component: './Home/Index',
    icon: 'home',
  },
  {
    path: '/account',
    name: '我的',
    component: './Home/UserDetail',
    icon: 'user',
  },

  {
    path: '/user',
    layout: false,
    routes: [
      { name: '登录', path: '/user/login', component: './User/Login' },
      { name: '注册', path: '/user/register', component: './User/Register' },
    ],
  },
  {
    path: '/admin',
    name: '平台管理',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        name: '用户管理',
        path: 'user-manage',
        component: './Admin/UserManager',
      },
      { name: '接口管理', path: 'interface-manage', component: './Admin/InterfaceInfo' },
      {
        name: '接口分析',
        path: 'interface-analysis',
        component: './Admin/InterfaceAnalysis',
      },
    ],
  },
  {
    path: '/interface-info/:id',
    name: '接口详情',
    component: './Home/InterfaceDetail',
    hideInMenu: true,
  },
  { path: '/', redirect: '/home' },
  { path: '*', layout: false, component: './404' },
];
