<template>
	<view class="box">
		<NavBar :isBack="false" title="选择性别" />
		<view class="value">
			<input type="text" v-model="value">
		</view>
		<view class="boxs u-flex">
			<view class="item" v-for="item in list" :key="item.dictLabel" @click="select(item)">
				{{item.dictLabel}}
			</view>
		</view>
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
		mapState,
		mapActions
	} from "vuex"
	import Api from '@/api/index.js'

	export default {
		components: {
			NavBar,
		},
		watch: {
			value() {
				this.getList()
			}
		},
		data() {
			return {
				value: "",
				selectItem: null,
				list: []
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			submit() {
				let _this = this;
				if (!this.value) {
					uni.showToast({
						title: '请选择性别',
						icon: 'error',
					})
					return
				}
				Api.putUserInfo({
					sex: _this.selectItem.dictCode == 1 ? 1 : 0
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
			getList() {
				Api.getDictList({
					pageNum: 1,
					pageSize: 10,
					dictType: 'sys_user_sex',
					dictLabel: this.value
				}, (res) => {
					const data = res.data.rows
					this.list = data
				})
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