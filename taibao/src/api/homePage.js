import request from '@/utils/request'
const prefix = 'TKNewsServlet/'

export function smartSearch(searchValue) {
  return request.post(prefix + 'SmartSearch?searchValue='+searchValue)
}
export function smartSearchPage(pageNumber,token) {
  return request.post(prefix + 'SmartSearchPage?page='+pageNumber+'&token='+token)
}
export function smartSearchEdge(code,token) {
  return request.post(prefix + 'SmartSearchEdge?code='+code+'&token='+token)
}


