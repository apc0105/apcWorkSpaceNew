import request from '@/utils/request'
const prefix = 'TKNewsServlet/'

export function smartSearch(pageSize,nDirection,searchValue,flPriceChng) {
  return request.post(prefix + 'SmartSearch?pageSize='+pageSize+'&nDirection='+nDirection+'&search_value='+searchValue+'&flPriceChng='+flPriceChng)
}
export function smartSearchPage(pageSize,nDirection,pageNumber,fPageNumber,token,flPriceChng) {
  return request.post(prefix + 'SmartSearchPage?pageSize='+pageSize+'&nDirection='+nDirection+'&pageNumber='+pageNumber+'&fPageNumber='+fPageNumber+'&token='+token+'&flPriceChng='+flPriceChng)
}
export function smartSearchEdge(code,token) {
  return request.post(prefix + 'SmartSearchEdge?code='+code+'&token='+token)
}
export function searchHintKeys(keyWords){
  return request.post(prefix + 'searchHintKeys?keyWords='+keyWords)
}

