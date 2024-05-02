import config from "@/common/lib/config.js"
import store from "../stroe/index.js"

const BaseUrl = config.baseUrl;
const Get = (path, data, success, err) => {
	uni.request({
		url: BaseUrl + path,
		method: 'GET',
		data,
		success(res) {
			success?.(res)
		},
		fail(err) {
			err?.(err)
		},
		header: {
			Authorization: store.state.token,
		}
	})
}

const Post = (path, data, success, err) => {
	uni.request({
		url: BaseUrl + path,
		method: 'POST',
		data,
		success(res) {
			success?.(res)
		},
		fail(err) {
			err?.(err)
		},
		header: {
			Authorization: store.state.token
		}
	})
}

const Put = (path, data, success, err) => {
	uni.request({
		url: BaseUrl + path,
		method: 'PUT',
		data,
		success(res) {
			success?.(res)
		},
		fail(err) {
			err?.(err)
		},
		header: {
			Authorization: store.state.token
		}
	})
}

const Delete = (path, data, success, err) => {
	uni.request({
		url: BaseUrl + path,
		method: 'DELETE',
		data,
		success(res) {
			success?.(res)
		},
		fail(err) {
			err?.(err)
		},
		header: {
			Authorization: store.state.token
		}
	})
}


const UploadImage = (file, success, err) => {
	uni.uploadFile({
		url: BaseUrl + '/system/oss/upload',
		filePath: file,
		name: 'file',
		success: (res) => {
			const fileObj = JSON.parse(res.data).data
			success({
				url: fileObj.url,
				fileName: fileObj.fileName
			})
		},
		fail(err) {
			console.log(err)
		},
		header: {
			Authorization: store.state.token
		}
	})
}

uni.addInterceptor('request', {
	success(args) {
		if (args.data.code === 401) {
			uni.redirectTo({
				url: '/pages/my/index'
			})
			store.dispatch('clearAuth')
			uni.showToast({
				title: '您尚未登录',
				icon: 'error'
			})
			throw "登录失效"
		}
		if (args.data.code == 500) {
			uni.showToast({
				title: args.data.msg,
				icon: 'none'
			})
			throw args.data.msg
		}
	},
	fail(err) {
		console.log('interceptor-fail', err)
	}
})

export {
	Post,
	Get,
	Put,
	Delete,
	UploadImage
}