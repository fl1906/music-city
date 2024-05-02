import request from '@/utils/request'

// 查询艺人列表
export function listArtist(query) {
  return request({
    url: '/system/artist/list',
    method: 'get',
    params: query
  })
}

// 查询艺人详细
export function getArtist(artistId) {
  return request({
    url: '/system/artist/' + artistId,
    method: 'get'
  })
}

// 新增艺人
export function addArtist(data) {
  return request({
    url: '/system/artist',
    method: 'post',
    data: data
  })
}

// 修改艺人
export function updateArtist(data) {
  return request({
    url: '/system/artist',
    method: 'put',
    data: data
  })
}

// 删除艺人
export function delArtist(artistId) {
  return request({
    url: '/system/artist/' + artistId,
    method: 'delete'
  })
}
