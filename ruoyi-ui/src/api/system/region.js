import request from '@/utils/request'

// 查询地区列表
export function listRegion(query) {
  return request({
    url: '/system/region/list',
    method: 'get',
    params: query
  })
}

// 查询地区详细
export function getRegion(regionId) {
  return request({
    url: '/system/region/' + regionId,
    method: 'get'
  })
}

// 新增地区
export function addRegion(data) {
  return request({
    url: '/system/region',
    method: 'post',
    data: data
  })
}

// 修改地区
export function updateRegion(data) {
  return request({
    url: '/system/region',
    method: 'put',
    data: data
  })
}

// 删除地区
export function delRegion(regionId) {
  return request({
    url: '/system/region/' + regionId,
    method: 'delete'
  })
}
