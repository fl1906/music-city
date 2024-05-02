<template>
	<view class="edit">
		<NavBar :isBack="false" title="编辑资料" />
		<Progress progress="45" class="progress" />
		<!-- 上传文件 -->
		<view class="u-flex upload">
			<image :src="userInfo.avatar" class="avatar" @click="uploadAvatar"></image>
			<u-upload :action="action" max-count="1" :custom-btn="true">
				<view slot="addBtn" class="slot-btn" hover-stay-time="150">
					<image src="../../../static/upload-icon.png"></image>
					<view>上传视频（选填）</view>
				</view>
			</u-upload>
		</view>
		<!-- 相册 -->
		<view class="card-title">相册</view>
		<view class="imgs u-flex">
			<view class="img-item u-flex" @click="uploadImage">
				<image src="../../../static/upload-icon.png"></image>
			</view>
			<view class="img-item standrd" v-for="item in photoList" :key="item.photoId">
				<image :src="item.url" />
			</view>
		</view>
		<!-- 基本资料 -->
		<view class="card-title">基本资料</view>
		<view class="card">
			<cell-item title="昵称">
				<text @click="onCellNavigate('name')">{{userInfo.nickname}}</text>
			</cell-item>
			<cell-item title="性别">
				<text @click="onCellNavigate('gender')">{{userInfo.sex ==0?'女':'男'}}</text>
			</cell-item>
			<cell-item title="年龄&星座">
				<text @click="onCellNavigate('ageAndCs')">
					{{userInfo.age}} {{userInfo.constellation}}
				</text>
			</cell-item>
			<cell-item title="地区">
				<text @click="()=>regionShow=true">
					{{userInfo.address?userInfo.address:"选择地区"}}
				</text>
			</cell-item>
			<u-picker mode="region" v-model="regionShow" :params="params" @confirm="selectRegion"></u-picker>

			<cell-item title="职业">
				<text @click="onCellNavigate('occupation')">
					{{userInfo.occupation?userInfo.occupation:"选择职业"}}
				</text>
			</cell-item>
			<cell-item title="学校">
				<text @click="onCellNavigate('school')">
					{{userInfo.school?userInfo.school:"选择学校"}}
				</text>
			</cell-item>
		</view>
		<!-- 个人标签 -->
		<view class="card-title">个人标签</view>
		<view class="card">
			<cell-item title="个人介绍">
				<text @click="onCellNavigate('desc')">
					{{userInfo.intro?userInfo.intro:'去填写'}}
				</text>
			</cell-item>
			<cell-item title="兴趣爱好">
				<text @click="onCellNavigate('userHobby')">
					{{userInfo.hobby?userInfo.hobby:'去填写'}}
				</text>
			</cell-item>
			<cell-item title="MBTI人格">
				<text @click="onCellNavigate('mbti')">
					{{userInfo.mbti?userInfo.mbti:'去填写'}}
				</text>
			</cell-item>
			<cell-item title="喜欢的音乐风格">
				<text @click="onCellNavigate('musicStyle')">
					{{userInfo.musicStyle?userInfo.musicStyle:'去填写'}}
				</text>
			</cell-item>
		</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Progress from "@/components/progress/progress.vue"
	import cellItem from "@/components/cell-item/cell-item.vue"
	import Api from "@/api/index.js"
	import {
		mapState,
		mapActions
	} from "vuex"

	export default {
		components: {
			NavBar,
			Progress,
			cellItem
		},
		computed: {
			...mapState(['userInfo'])
		},
		data() {
			return {
				photoList: [],
				params: {
					province: true,
					city: true,
					area: false
				},
				regionShow: false
			}
		},
		methods: {
			...mapActions(['getUserInfo']),
			// 上传头像
			uploadAvatar() {
				let _this = this;
				Api.postImageFile(res => {
					const path = res.url
					Api.putUserInfo({
						avatar: path
					}, () => {
						_this.getUserInfo();
					})
				})
			},
			// cell 批量跳转
			onCellNavigate(path) {
				uni.navigateTo({
					url: `/pages/my/edit/${path}/index`
				})
			},
			// 上传相册
			uploadImage() {
				Api.postImageFile(res => {
					const path = res.url
					Api.postImageUrlsToPhoto(path)
				})
			},
			// 获取相册列表
			getPhotoList() {
				Api.getUserImageList({}, (res) => {
					const data = res.data.rows
					this.photoList = data
					this.getUserInfo()
				})
			},
			// 选择地区
			selectRegion(res) {
				const {
					city,
					province
				} = res
				Api.putUserInfo({
					address: `${province.label}·${city.label}`
				}, () => {
					this.getUserInfo();
				})
			}
		},
		onLoad() {
			this.getPhotoList()
		}
	}
</script>

<style>
	@import url('index.css');
</style>