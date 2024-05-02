<template>
	<view class="music-style">
		<NavBar :isBack="false" title="喜欢的音乐风格" />
		<view class="top u-flex">
			<view class="search u-flex">
				<u-icon name="search" color="#929097"></u-icon>
				<input type="text" v-model="value">
			</view>
			<view class="cancel" @click="goBack">取消</view>
		</view>
		<scroll-view class="list" :scroll-y="true" @scrolltolower="scrollBottom">
			<view class="list-item" v-for="item in list" :key="item.dictLabel" @click="select(item)">
				{{item.dictLabel}}
			</view>
		</scroll-view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
		<button class="submit" @click="submit">保存</button>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import {
		mapActions
	} from "vuex"
	import Api from "@/api/index.js"
	export default {
		components: {
			NavBar,
		},
		watch: {
			value() {
				this.getList()
			},
			pageNum() {
				if (!this.hasMore) return
				this.getList()
			}
		},
		data() {
			return {
				value: "",
				list: [],
				pageNum: 1,
				selectItem: null,
				hasMore: false
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			submit() {
				let _this = this;
				if (!this.selectItem) {
					uni.showToast({
						title: '请选择音乐风格',
						icon: 'error',
					})
					return
				}
				Api.putUserInfo({
					musicStyle: this.selectItem.dictValue
				}, () => {
					_this.getUserInfo();
					uni.navigateBack({
						delta: 1
					})
				})
			},
			select(item) {
				this.value = item.dictLabel
				this.selectItem = item
			},
			goBack() {
				uni.navigateBack()
			},
			getList() {
				Api.getDictList({
					pageNum: this.pageNum,
					pageSize: 20,
					dictType: 'music_style',
					dictLabel: this.value
				}, (res) => {
					const data = res.data.rows
					const length = res.data.total
					this.list.length < length ? this.hasMore = true : this.hasMore = false
					this.list = this.list.concat(data)
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