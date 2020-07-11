const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  nickname: state => state.user.nickname,
  username: state => state.user.username,
  roles: state => state.user.roles,
  menus: state => state.user.menus,
  permission_add_routes: state => state.permission.addRoutes,
  permission_routes: state => state.permission.routes,
  permission_route_paths: state => state.permission.routePaths,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
}
export default getters
