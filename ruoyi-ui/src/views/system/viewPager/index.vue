<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="轮播图名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入轮播图名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="轮播图链接Url" prop="linkUrl">
        <el-input
          v-model="queryParams.linkUrl"
          placeholder="请输入轮播图链接Url"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="删除状态，默认为1表示正常状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder="请输入删除状态，默认为1表示正常状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联活动id  0表示不关联" prop="activityId">
        <el-input
          v-model="queryParams.activityId"
          placeholder="请输入关联活动id  0表示不关联"
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
          v-hasPermi="['system:viewPager:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:viewPager:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:viewPager:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:viewPager:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="viewPagerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="轮播图id" align="center" prop="viewPagerId" v-if="true"/>
      <el-table-column label="轮播图名称" align="center" prop="name" />
      <el-table-column label="轮播图图片Url" align="center" prop="imageUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="轮播图链接Url" align="center" prop="linkUrl" />
      <el-table-column label="删除状态，默认为1表示正常状态" align="center" prop="state" />
      <el-table-column label="关联活动id  0表示不关联" align="center" prop="activityId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:viewPager:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:viewPager:remove']"
          >删除</el-button>
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

    <!-- 添加或修改轮播图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="轮播图名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入轮播图名称" />
        </el-form-item>
        <el-form-item label="轮播图图片Url" prop="imageUrl">
          <image-upload v-model="form.imageUrl"/>
        </el-form-item>
        <el-form-item label="轮播图链接Url" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="请输入轮播图链接Url" />
        </el-form-item>
        <el-form-item label="删除状态，默认为1表示正常状态" prop="state">
          <el-input v-model="form.state" placeholder="请输入删除状态，默认为1表示正常状态" />
        </el-form-item>
        <el-form-item label="关联活动id  0表示不关联" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入关联活动id  0表示不关联" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listViewPager, getViewPager, delViewPager, addViewPager, updateViewPager } from "@/api/system/viewPager";

export default {
  name: "ViewPager",
  data() {
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
      // 轮播图表格数据
      viewPagerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 关联活动id  0表示不关联时间范围
      daterangeCreateTime: [],
      // 关联活动id  0表示不关联时间范围
      daterangeUpdateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        imageUrl: undefined,
        linkUrl: undefined,
        state: undefined,
        activityId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        viewPagerId: [
          { required: true, message: "轮播图id不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "轮播图名称不能为空", trigger: "blur" }
        ],
        imageUrl: [
          { required: true, message: "轮播图图片Url不能为空", trigger: "blur" }
        ],
        linkUrl: [
          { required: true, message: "轮播图链接Url不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        state: [
          { required: true, message: "删除状态，默认为1表示正常状态不能为空", trigger: "blur" }
        ],
        activityId: [
          { required: true, message: "关联活动id  0表示不关联不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询轮播图列表 */
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
      listViewPager(this.queryParams).then(response => {
        this.viewPagerList = response.rows;
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
        viewPagerId: undefined,
        name: undefined,
        imageUrl: undefined,
        linkUrl: undefined,
        createTime: undefined,
        updateTime: undefined,
        state: undefined,
        activityId: undefined
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
      this.ids = selection.map(item => item.viewPagerId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加轮播图";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const viewPagerId = row.viewPagerId || this.ids
      getViewPager(viewPagerId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改轮播图";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.viewPagerId != null) {
            updateViewPager(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addViewPager(this.form).then(response => {
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
      const viewPagerIds = row.viewPagerId || this.ids;
      this.$modal.confirm('是否确认删除轮播图编号为"' + viewPagerIds + '"的数据项？').then(() => {
        this.loading = true;
        return delViewPager(viewPagerIds);
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
      this.download('system/viewPager/export', {
        ...this.queryParams
      }, `viewPager_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
