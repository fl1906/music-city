<template>
	<view class="box">
		<NavBar :isBack="false" title="兴趣爱好" />
		<view class="value">
			<view>{{value.join(" ")}}</view>
		</view>
		<ScrollList @scrollBottom="scrollBottom" height="60vh">
			<view class="item" v-for="item in list" :key="item.dictLabel" @click="select(item)">
				{{item.dictLabel}}
			</view>
		</ScrollList>
		<button class="submit" @click="submit">保存</button>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import {
		mapState,
		mapActions
	} from "vuex"
	import ScrollList from "@/components/scroll-list/scroll-list.vue"
	import Api from '@/api/index.js'

	export default {
		components: {
			NavBar,
			ScrollList
		},
		watch: {
			pageNum() {
				if (!this.hasMore) return
				this.getList()
			}
		},
		data() {
			return {
				value: [],
				selectItem: null,
				list: [],
				pageNum: 1,
				hasMore: false
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			submit() {
				let _this = this;
				if (!this.selectItem) {
					uni.showToast({
						title: '请填写所有项',
						icon: 'error',
					})
					return
				}
				Api.putUserInfo({
					hobby: this.value.join(" "),
				}, () => {
					_this.getUserInfo();
					uni.navigateBack({
						delta: 1
					})
				})
			},
			select(item) {
				if (this.value.find(name => name === item.dictLabel)) return
				this.value.push(item.dictLabel)
				this.selectItem = item
			},
			getList() {
				Api.getDictList({
					pageNum: this.pageNum,
					pageSize: 20,
					dictType: 'user_hobby',
				}, (res) => {
					const data = res.data.rows
					const length = res.data.total
					this.list = this.list.concat(data)
					this.list.length < length ? this.hasMore = true : this.hasMore = false
				})
			},
			scrollBottom() {
				this.pageNum += 1
			}
		},
		onLoad() {
			this.getList()
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>