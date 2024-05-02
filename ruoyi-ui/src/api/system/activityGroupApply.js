import request from '@/utils/request'

// 查询活动组队申请列列表
export function listActivityGroupApply(query) {
  return request({
    url: '/system/activityGroupApply/list',
    method: 'get',
    params: query
  })
}

// 查询活动组队申请列详细
export function getActivityGroupApply(applyId) {
  return request({
    url: '/system/activityGroupApply/' + applyId,
    method: 'get'
  })
}

// 新增活动组队申请列
export function addActivityGroupApply(data) {
  return request({
    url: '/system/activityGroupApply',
    method: 'post',
    data: data
  })
}

// 修改活动组队申请列
export function updateActivityGroupApply(data) {
  return request({
    url: '/system/activityGroupApply',
    method: 'put',
    data: data
  })
}

// 删除活动组队申请列
export function delActivityGroupApply(applyId) {
  return request({
    url: '/system/activityGroupApply/' + applyId,
    method: 'delete'
  })
}
