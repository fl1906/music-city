<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" label-width="120px" :rules="rules">
      <el-form-item label="活动标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入活动标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--form-item 放在一列-->
      <div class="flex">
        <el-form-item label="所在城市" prop="regionId">
          <el-select filterable v-model="queryParams.regionId" placeholder="请选择">
            <el-option
              v-for="item in addressList"
              :key="item.regionId"
              :label="item.name"
              :value="item.regionId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="classify">
          <el-select v-model="queryParams.classify" placeholder="请选择">
            <el-option
              v-for="item in classifyList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="地区" prop="classify">
          <el-select filterable v-model="queryParams.region" placeholder="请选择">
            <el-option
              v-for="item in region"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="艺人" prop="artistList">
          <el-select filterable v-model="queryParams.artistList" placeholder="请选择" multiple>
            <el-option
              v-for="item in listArtist"
              :key="item.artistId"
              :label="item.name"
              :value="item.artistId">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="flex">
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="queryParams.startTime"
            type="datetime"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker
            v-model="queryParams.endDate"
            type="datetime"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <!--        <el-form-item label="售票结束时间" prop="saleEndTime">-->
        <!--          <el-date-picker-->
        <!--            v-model="queryParams.saleEndTime"-->
        <!--            type="datetime"-->
        <!--            placeholder="选择日期"-->
        <!--            value-format="yyyy-MM-dd HH:mm:ss"-->
        <!--          >-->
        <!--          </el-date-picker>-->
        <!--        </el-form-item>-->
        <el-form-item label="展示时间" prop="showTime">
          <el-date-picker
            v-model="queryParams.showTime"
            type="datetime"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <el-form-item label="活动主办方" prop="organizerLists">
        <el-select filterable v-model="queryParams.organizerLists" placeholder="请选择">
          <el-option
            v-for="item in listOrganizer"
            :key="item.organizerId"
            :label="item.name"
            :value="item.organizerId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        v-for="(item, index) in queryParams.organizerTickets"
        :label="'票务' + (index + 1)"
        :key="index"
      >
        <label>名称：</label>
        <el-input v-model="item.name" class="input"></el-input>
        <label>购票二维码：</label>
        <image-upload v-model="item.qrImage"/>
        <label>Logo：</label>
        <image-upload v-model="item.logoImage"/>
        <el-button @click.prevent="deleteTicket(index)">删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="addTicket">新增票务</el-button>
      </el-form-item>
      <div class="flex">
        <el-form-item label="舞台介绍" prop="introList">
          <el-select filterable v-model="queryParams.stageList" placeholder="请选择" multiple>
            <el-option
              v-for="item in wtIntro"
              :key="item.introId"
              :label="item.title"
              :value="item.introId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="更多介绍" prop="introList">
          <el-select filterable v-model="queryParams.introList" placeholder="请选择" multiple>
            <el-option
              v-for="item in listIntro"
              :key="item.introId"
              :label="item.title"
              :value="item.introId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动标签" prop="tagList">
          <el-select v-model="queryParams.tagList" placeholder="请选择" multiple>
            <el-option
              v-for="item in listTag"
              :key="item.tagId"
              :label="item.name"
              :value="item.tagId">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <el-form-item label="活动详情主图" prop="innerImage">
        <image-upload v-model="queryParams.innerImage"/>
      </el-form-item>
      <el-form-item label="活动封面" prop="coverImage">
        <image-upload v-model="queryParams.coverImage"/>
      </el-form-item>
      <el-form-item label="活动海报" prop="shareImage">
        <image-upload v-model="queryParams.shareImage"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit" :loading="buttonLoading">{{ submitText }}</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {addActivity, getActivity, getAddressList, updateActivity} from "@/api/system/activity";
import {listArtist} from "@/api/system/artist";
import {listIntro} from "@/api/system/intro";
import {listTag} from "@/api/system/tag";
import {listOrganizer} from "@/api/system/organizer";
import dayjs from "dayjs";

export default {
  data() {
    return {
      // 提交按钮文案
      submitText: '立即创建',
      // 分类列表
      classifyList: [
        {value: 0, label: '电音节'},
        {value: 1, label: '派对'},
      ],
      region: [
        {value: 0, label: "国际"},
        {value: 1, label: '国内'}
      ],
      // 地址列表
      addressList: [],
      // 艺人列表
      listArtist: [],
      // 活动介绍列表
      listIntro: [],
      wtIntro: [],
      // 标签列表
      listTag: [],
      // 主办方列表
      listOrganizer: [],
      // 提交按钮loading
      buttonLoading: false,
      // 查询参数
      queryParams: {
        artistList: [],
        introList: [],
        tagList: [],
        organizerLists: null,
        organizerTickets: [
          {name: '', qrImage: '', logoImage: ''}
        ],
        stageList: [],
        address: undefined,
        region: undefined,
        regionId: undefined,
        classify: undefined,
        saleEndTime: '2023/8/4 12:00:00',
        title: undefined,
        startTime: undefined,
        endDate: undefined,
        innerImage: undefined,
        showTime: undefined,
        coverImage: undefined,
        shareImage: undefined,
        createTime: undefined,
        updateTime: undefined,
      },
      // 表单校验
      rules: {
        address: [
          {required: true, message: "地址不能为空", trigger: "blur"}
        ],
        regionId: [
          {required: true, message: "城市ID不能为空", trigger: "blur"}
        ],
        title: [
          {required: true, message: "活动标题不能为空", trigger: "blur"}
        ],
        classify: [
          {required: true, message: "分类不能为空", trigger: "blur"}
        ],
        region: [
          {required: true, message: "地区不能为空", trigger: "blur"}
        ],
        artistList: [
          {required: true, message: "艺人不能为空", trigger: "blur"}
        ],
        // saleEndTime: [
        //   {required: true, message: "售票结束时间不能为空", trigger: "blur"}
        // ],
        startTime: [
          {required: true, message: "开始时间不能为空", trigger: "blur"}
        ],
        endDate: [
          {required: true, message: "结束时间不能为空", trigger: "blur"}
        ],
        innerImage: [
          {required: true, message: "活动详情主图不能为空", trigger: "blur"}
        ],
        showTime: [
          {required: true, message: "展示时间不能为空", trigger: "blur"}
        ],
        coverImage: [
          {required: true, message: "封面图片不能为空", trigger: "blur"}
        ],
        organizerLists: [
          {required: true, message: "主办方不能为空", trigger: "blur"}
        ],
        introList: [
          {required: true, message: "活动介绍不能为空", trigger: "blur"}
        ],
        tagList: [
          {required: true, message: "活动标签不能为空", trigger: "blur"}
        ],
        organizerTickets: [
          {required: true, message: "主办方票务不能为空", trigger: "blur"}
        ],
        shareImage: [
          {required: true, message: "活动海报不能为空", trigger: "blur"}
        ],
      }
    }
  },
  methods: {
    // 删除票务
    deleteTicket(index) {
      this.queryParams.organizerTickets.splice(index, 1)
    },
    // 添加票务
    addTicket() {
      this.queryParams.organizerTickets.push({
        name: "",
        qrImage: "",
        logoImage: "",
      })
    },
    /*获取详情数据*/
    async getDetail(activityId) {
      if (activityId === undefined) return
      const data = await getActivity(activityId)
      this.queryParams = {
        ...data.data,
        organizerLists: data.data.organizerList.organizerId,
        organizerTickets: data.data.organizerList.organizerTickets,
        tagList: data.data.tagList.map(item => item.tagId),
        introList: data.data.introList.map(item => item.introId),
        stageList: data.data.stageList.map(item => item.introId),
        artistList: data.data.artistList.map(item => item.artistId),

      }
      this.submitText = '立即修改'
    },
    /*查询地址列表*/
    async handleQuery() {
      const data = await getAddressList()
      this.addressList = data.data['全部']
    },
    /*查询艺人列表*/
    async queryArtistList() {
      const data = await listArtist()
      this.listArtist = data.rows
    },
    /*查询活动介绍列表*/
    async queryListIntro() {
      const data = await listIntro()
      data.rows.forEach(item => {
        if (item.type === 0) {
          this.wtIntro.push(item)
        }
        if (item.type === 1) {
          this.listIntro.push(item)
        }
      })
      // this.listIntro = data.rows
    },
    /*查询标签列表*/
    async queryListTag() {
      const data = await listTag()
      this.listTag = data.rows
    },
    /*查询活动主办方*/
    async queryListOrganizer() {
      const data = await listOrganizer()
      this.listOrganizer = data.rows
    },
    /*创建活动*/
    onSubmit() {
      const now = dayjs().format('YYYY-MM-DD HH:mm:ss')

      this.$refs["queryForm"].validate(valid => {
        this.queryParams.artistList = this.queryParams.artistList.map(item => ({artistId: item}))
        this.queryParams.introList = this.queryParams.introList.map(item => ({introId: item}))
        this.queryParams.tagList = this.queryParams.tagList.map(item => ({tagId: item}))
        this.queryParams.stageList = this.queryParams.stageList.map(item => ({introId: item}))
        this.buttonLoading = true;
        const organizerId = this.queryParams.organizerLists
        const organizer = this.listOrganizer.find(item => item.organizerId === organizerId)
        delete organizer.organizerId
        const organizerList = {
          ...organizer,
          organizerId: organizerId,
          organizerTickets: this.queryParams.organizerTickets
        }

        if (this.queryParams.activityId) {
          updateActivity({...this.queryParams, updateTime: now, organizerList}).then(response => {
            this.$modal.msgSuccess("修改成功");
            //跳转到活动列表页面 并刷新活动列表
            this.$router.push({path: '/pzcActivity/activity'})
          })
            .finally(() => {
              this.buttonLoading = false;
            });
        } else {
          addActivity({...this.queryParams, createTime: now, organizerList}).then(response => {
            this.$modal.msgSuccess("新增成功");
            //跳转到活动列表页面 并刷新活动列表
            this.$router.push({path: '/pzcActivity/activity'})


          }).finally(() => {
            this.buttonLoading = false;
          });
        }
      })
    },
  },
  created() {
    this.handleQuery()
    this.queryArtistList()
    this.queryListIntro()
    this.queryListTag()
    this.queryListOrganizer()
    const activityId = this.$route.query.activityId
    this.queryParams.activityId = activityId
    this.getDetail(activityId)
  }
}
</script>

<style>
.flex {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.input {
  display: block;
  width: 120px;
  margin-right: 10px;
}
</style>
