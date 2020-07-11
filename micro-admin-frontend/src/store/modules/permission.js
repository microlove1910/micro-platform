import {asyncRoutes, constantRoutes} from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param menus
 * @param route
 */
function hasRoute(menus, route) {
  return menus.some(r => {
    return route.path + route.name == r.path + r.name;
  })
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param menus
 * @param childrenMap
 */
export function filterAsyncRoutes(routes, menus, childrenMap) {
  return routes.filter(route => {
    if (hasRoute(menus, route)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRoutes(route.children, childrenMap[route.path + route.name])
      }
      return true;
    }
    return false;
  });
}

const state = {
  routes: [],
  addRoutes: [],
  routePaths: [],
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
    const routePaths = []
    for (const r of state.routes) {
      routePaths.push(r.path);
      if (!(r.children && r.children.length)) {
        continue
      }
      for (const c of r.children) {
        if (r.path === '/') {
          routePaths.push(r.path + c.path);
        } else {
          routePaths.push(r.path + '/' + c.path);
        }
      }
    }
    routePaths.push('/redirect/dashboard')
    state.routePaths = routePaths;
  },
  RESET_ROUTE: (state) => {
    console.log('清除路由信息');
    state.addRoutes = [];
    state.routes = [];
    state.routePaths = []
  }
}

const actions = {
  generateRoutes({commit}, {roles, menus}) {
    return new Promise(resolve => {
      var accessedRoutes = []
      if (roles.some(r => r.code === 'root')) {
        accessedRoutes = asyncRoutes || []
      } else {
        const childrenMap = {};
        for (const p of menus) {
          childrenMap[p.path + p.name] = p.children;
        }
        accessedRoutes = filterAsyncRoutes(asyncRoutes, menus, childrenMap)
        accessedRoutes.push({path: '*', redirect: '/404', name: '404', hidden: true})
      }
      console.log(accessedRoutes, asyncRoutes);
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  },
  clearRoutes({commit}) {
    return new Promise(resolve => {
      commit('RESET_ROUTE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
