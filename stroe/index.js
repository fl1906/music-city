import Vue from 'vue'
import Vuex from 'vuex'
import conf from '@/common/lib/config.js'
import io from '@hyoga/uni-socket.io'
import Api from '@/api/index.js'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		userInfo: null,
		token: null,
		socket: null,
		messing: false,
		moreTimeShow: false,
		officialApplyId: null,
		loading: true,
		// 悬浮音乐的显示状态
		hoverMusic: false,
		// 音频实例
		audio: null
	},
	mutations: {
		setUserInfo(state, userInfo) {
			state.userInfo = userInfo
		},
		setToken(state, token) {
			state.token = token
		},
		clearAuth(state) {
			state.token = null
			state.userInfo = null
			uni.clearStorage()
		},
		setMoreTime(state, show) {
			state.moreTimeShow = show
		},
		setLoading(state, val) {
			state.loading = val
		},
		setHoverMusic(state, val) {
			state.hoverMusic = val
		}
	},
	actions: {
		getMusic({
			dispatch
		}) {
			Api.getMusic((res) => {
				const id = res.data.msg
				dispatch('connectAudio', id)
			})
		},
		setHoverMusic({
			commit
		}, val) {
			commit('setHoverMusic', val)
		},
		setPlayOrPause({
			commit,
			state
		}) {
			if (state.audio.paused) { // 暂停
				state.audio.play()
			} else {
				state.audio.pause()
			}
		},
		setLoading({
			commit
		}, data) {
			commit('setLoading', data)
		},
		closeMoreTime(content) {
			content.commit('setMoreTime', false)
		},
		clearAuth(content) {
			content.commit('clearAuth')
		},
		getUserInfo(content) {
			Api.getUserInfo((res) => {
				const userInfo = res.data.data
				content.commit('setUserInfo', userInfo)
				uni.setStorage({
					key: 'userInfo',
					data: userInfo
				})
			})
		},
		setUserInfo(content, userInfo) {
			content.commit('setUserInfo', userInfo)
		},
		setToken(content, token) {
			content.commit('setToken', token)
		},
		connectAudio({
			state,
			dispatch
		}, data) {
			const audio = uni.createInnerAudioContext()
			state.audio = audio
			const srcS = data.includes('http')
			audio.src = srcS ? data : `http://v.api.aa1.cn/api/wymusic/index.php?id=${data}`
			audio.loop = true
			audio.volume = 0.4
			audio.onTimeUpdate(() => {
				uni.setStorageSync("currentTime", audio.currentTime)
			})
			const currentTime = uni.getStorageSync("currentTime")
			if (currentTime) {
				audio.seek(currentTime)
			}
			audio.autoplay = true
		},
		//关闭连接
		closeSocket({
			state,
			dispatch
		}) {
			if (state.socket !== null) {
				state.socket.close()
				state.socket = null
			}
		},
		//发送信息
		sendMsg({
			state,
			dispatch
		}, data) {
			console.log(data);
			state.socket.emit('chat', data)
		},
		// 发送超限制到达
		sendMoreTime({
			state,
			dispatch
		}, data) {
			state.socket.emit('official', data)
		},
		// 接受信息
		acceptInfo({}, fn) {
			fn()
		},
		connectSocket({
			state,
			dispatch,
			commit
		}, data) {
			const socket = io(conf.socketUrl, {
				//这里是连接时所携带的数据
				query: data,
				//连接方式
				transports: ['websocket'],
				//过期时间
				timeout: 5000,
			});
			socket.on('connect', () => {
				console.log('ws 已连接');
				state.socket = socket
				// 监听服务器连接成功后的状态
				socket.on('chat', function(data) {
					state.messing = true
				});
			});
			socket.on('official', (data) => {
				const {
					applyId
				} = data
				state.officialApplyId = applyId
				commit('setMoreTime', true)
			});
			//监听服务器的断开连接重连方法
			socket.on('disconnect', () => {
				console.log("断开链接");
				this.dispatch('closeSocket')
			});
			//监听服务器出现错误的方法
			socket.on('error', (msg) => {
				console.log('ws error', msg);
				this.dispatch('closeSocket')
			});
		},
	}
})

export default store