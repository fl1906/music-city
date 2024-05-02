<script>
	import {
		mapMutations,
		mapState,
		mapActions
	} from "vuex"
	export default {
		computed: {
			
			...mapState(['socket', 'audio'])
		},
		methods: {
			...mapActions(['connectSocket', 'setHoverMusic', 'getMusic']),
			...mapMutations(['setToken', 'setUserInfo'])
		},
		onLaunch: function() {
			const userInfo = uni.getStorageSync("userInfo")
			const token = uni.getStorageSync('token')
			if (userInfo) {
				this.setUserInfo(userInfo)
				this.connectSocket({
					userId: userInfo.userId
				})
			}
			if (token) {
				this.setToken(token)
			}
			this.setHoverMusic(true)
			this.getMusic()
		},
		onShow: function() {
			console.log('App Show')
			if (this.socket) return

			const userInfo = uni.getStorageSync("userInfo")
			if (userInfo) {
				this.connectSocket({
					userId: userInfo.userId
				})
			}
		},
		onHide: function() {
			console.log(this.audio.currentTime)
		},
	}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";

	page {
		min-height: 100%;
		background-color: #150C24 !important;
	}

	view {
		position: relative;
	}

	/deep/ .u-loading {
		position: absolute;
		left: 50%;
		top: 50%;
	}
</style>