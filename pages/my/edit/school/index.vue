<template>
	<view class="school">
		<NavBar :isBack="false" title="选择学校" />
		<view class="value">
			<input type="text" v-model="value">
		</view>
		<view class="schools u-flex">
			<view class="item" v-for="item in list" :key="item.school_name" @click="select(item)">
				{{item.school_name}}
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
				Api.getSchoolList({
					schoolName: this.value
				}, (res) => {
					const data = res.data.data.schools
					this.list = data
				})
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
						title: '请选择学校',
						icon: 'error',
					})
					return
				}
				Api.putUserInfo({
					school: _this.value
				}, () => {
					_this.getUserInfo();
					uni.navigateBack({
						delta: 1
					})
				})
			},
			select(school) {
				this.value = school.school_name
				this.selectItem = school
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>