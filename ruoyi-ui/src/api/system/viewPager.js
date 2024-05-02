import request from '@/utils/request'

// 查询轮播图列表
export function listViewPager(query) {
  return request({
    url: '/system/viewPager/list',
    method: 'get',
    params: query
  })
}

// 查询轮播图详细
export function getViewPager(viewPagerId) {
  return request({
    url: '/system/viewPager/' + viewPagerId,
    method: 'get'
  })
}

// 新增轮播图
export function addViewPager(data) {
  return request({
    url: '/system/viewPager',
    method: 'post',
    data: data
  })
}

// 修改轮播图
export function updateViewPager(data) {
  return request({
    url: '/system/viewPager',
    method: 'put',
    data: data
  })
}

// 删除轮播图
export function delViewPager(viewPagerId) {
  return request({
    url: '/system/viewPager/' + viewPagerId,
    method: 'delete'
  })
}
