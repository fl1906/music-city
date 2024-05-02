<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="城市ID" prop="regionId">
        <el-input
          v-model="queryParams.regionId"
          placeholder="请输入城市ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入活动标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-input
          v-model="queryParams.startTime"
          placeholder="请输入开始时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endDate">
        <el-input
          v-model="queryParams.endDate"
          placeholder="请输入结束时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动详情主图" prop="innerImage">
        <el-input
          v-model="queryParams.innerImage"
          placeholder="请输入活动详情主图"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="展示时间" prop="showTime">
        <el-input
          v-model="queryParams.showTime"
          placeholder="请输入展示时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间">
        <el-date-picker
          v-model="daterangeUpdateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="删除状态，默认为1表示正常状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder="请输入删除状态，默认为1表示正常状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:activity:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:activity:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:activity:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:activity:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="activityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="活动id" align="center" prop="activityId" v-if="true"/>
      <el-table-column label="活动标题" align="center" prop="title"/>
      <el-table-column label="活动类型" align="center" prop="classify">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.activity_type" :value="scope.row.classify"/>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="address"/>
      <el-table-column label="城市ID" align="center" prop="regionId"/>
      <el-table-column label="开始时间" align="center" prop="startTime"/>
      <el-table-column label="结束时间" align="center" prop="endDate"/>
      <el-table-column label="活动详情主图" align="center" prop="innerImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.innerImage" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="封面图片" align="center" prop="coverImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverImage" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="海报图片" align="center" prop="shareImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.shareImage" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="展示时间" align="center" prop="showTime"/>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="更新时间" align="center" prop="updateTime"/>

      <!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
      <!--        <template slot-scope="scope">-->
      <!--          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">-->
      <!--        <template slot-scope="scope">-->
      <!--          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="删除状态，默认为1表示正常状态" align="center" prop="state"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:activity:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:activity:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改活动对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="活动地址" prop="address">
          <el-input v-model="form.address" placeholder="请选择活动地址"/>
        </el-form-item>
        <el-form-item label="城市ID" prop="regionId">
          <el-input v-model="form.regionId" placeholder="请输入城市ID"/>
        </el-form-item>
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题"/>
        </el-form-item>
        <el-form-item label="活动起止时间" prop="startTime">
          <!--          <el-input v-model="form.startTime" placeholder="请输入开始时间"/>-->

          <el-date-picker
            v-model="form.rangeTime"
            type="datetimerange"
            range-separator="至"
            start-placeholder="活动开始时间"
            end-placeholder="活动结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <!--        <el-form-item label="结束时间" prop="endDate">-->
        <!--          <el-input v-model="form.endDate" placeholder="请输入结束时间"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="活动详情主图" prop="innerImage">
          <el-input v-model="form.innerImage" placeholder="请输入活动详情主图"/>
        </el-form-item>
        <el-form-item label="展示时间" prop="showTime">
          <el-input v-model="form.showTime" placeholder="请输入展示时间"/>
        </el-form-item>
        <el-form-item label="封面图片" prop="coverImage">
          <image-upload v-model="form.coverImage"/>
        </el-form-item>
        <!--        <el-form-item label="删除状态，默认为1表示正常状态" prop="state">-->
        <!--          <el-input v-model="form.state" placeholder="请输入删除状态，默认为1表示正常状态"/>-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listActivity, getActivity, delActivity, addActivity, updateActivity} from "@/api/system/activity";

export default {
  name: "Activity",
  dicts: ['activity_type'],
  data() { //这里data()定义为函数的原因是，如果是对象的话，会导致多个组件共享一个data，从而导致数据错乱
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 活动表格数据
      activityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 删除状态，默认为1表示正常状态时间范围
      daterangeCreateTime: [],
      // 删除状态，默认为1表示正常状态时间范围
      daterangeUpdateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        address: undefined,
        regionId: undefined,
        title: undefined,
        classify: undefined,
        rangeTime: [],
        startTime: undefined,
        endDate: undefined,
        innerImage: undefined,
        showTime: undefined,
        coverImage: undefined,
        createTime: undefined,
        updateTime: undefined,
        state: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        activityId: [
          {required: true, message: "活动id不能为空", trigger: "blur"}
        ],
        address: [
          {required: true, message: "地址不能为空", trigger: "blur"}
        ],
        regionId: [
          {required: true, message: "城市ID不能为空", trigger: "blur"}
        ],
        title: [
          {required: true, message: "活动标题不能为空", trigger: "blur"}
        ],
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询活动列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      if (null != this.daterangeUpdateTime && '' != this.daterangeUpdateTime) {
        this.queryParams.params["beginUpdateTime"] = this.daterangeUpdateTime[0];
        this.queryParams.params["endUpdateTime"] = this.daterangeUpdateTime[1];
      }
      listActivity(this.queryParams).then(response => {
        this.activityList = response.rows;
        console.log(this.activityList)
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        activityId: undefined,
        address: undefined,
        regionId: undefined,
        title: undefined,
        classify: undefined,
        startTime: undefined,
        endDate: undefined,
        innerImage: undefined,
        showTime: undefined,
        coverImage: undefined,
        createTime: undefined,
        updateTime: undefined,
        state: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.daterangeUpdateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.activityId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: "/pzcActivity/add"})
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({path: "/pzcActivity/add", query: {activityId: row.activityId}})
      // this.loading = true;
      // this.reset();
      // const activityId = row.activityId || this.ids
      // getActivity(activityId).then(response => {
      //   this.loading = false;
      //   this.form = response.data;
      //   this.open = true;
      //   this.title = "修改活动";
      // });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.activityId != null) {
            console.log("活动起止时间是： " + this.form.rangeTime)
            this.form.startTime = this.form.rangeTime[0];
            this.form.endDate = this.form.rangeTime[1];
            updateActivity(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addActivity(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const activityIds = row.activityId || this.ids;
      this.$modal.confirm('是否确认删除活动编号为"' + activityIds + '"的数据项？').then(() => {
        this.loading = true;
        return delActivity(activityIds);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/activity/export', {
        ...this.queryParams
      }, `activity_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>


<style>
</style>
