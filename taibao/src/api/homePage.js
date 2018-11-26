import request from '@/utils/request'
const prefix = 'TKNewsServlet/'

export function smartSearch(nDirection,searchValue) {
  return request.post(prefix + 'SmartSearch?nDirection='+nDirection+'&search_value='+searchValue)
}
export function smartSearchPage(nDirection,pageNumber,token) {
  return request.post(prefix + 'SmartSearchPage?nDirection='+nDirection+'&pageNumber='+pageNumber+'&token='+token)
}
export function smartSearchEdge(code,token) {
  return request.post(prefix + 'SmartSearchEdge?code='+code+'&token='+token)
}


