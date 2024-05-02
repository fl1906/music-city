import request from '@/utils/request'

// 查询活动标签列表
export function listTag(query) {
  return request({
    url: '/system/tag/list',
    method: 'get',
    params: query
  })
}

// 查询活动标签详细
export function getTag(tagId) {
  return request({
    url: '/system/tag/' + tagId,
    method: 'get'
  })
}

// 新增活动标签
export function addTag(data) {
  return request({
    url: '/system/tag',
    method: 'post',
    data: data
  })
}

// 修改活动标签
export function updateTag(data) {
  return request({
    url: '/system/tag',
    method: 'put',
    data: data
  })
}

// 删除活动标签
export function delTag(tagId) {
  return request({
    url: '/system/tag/' + tagId,
    method: 'delete'
  })
}
