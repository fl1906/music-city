<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联用户Id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入关联用户Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联活动Id" prop="activityId">
        <el-input
          v-model="queryParams.activityId"
          placeholder="请输入关联活动Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择操作类型" clearable>
          <el-option
            v-for="dict in dict.type.user_history_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="信息" prop="message">
        <el-input
          v-model="queryParams.message"
          placeholder="请输入信息"
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
          v-hasPermi="['system:userHistory:add']"
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
          v-hasPermi="['system:userHistory:edit']"
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
          v-hasPermi="['system:userHistory:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:userHistory:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userHistoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="historyId" v-if="true"/>
      <el-table-column label="关联用户Id" align="center" prop="userId" />
      <el-table-column label="关联活动Id" align="center" prop="activityId" />
      <el-table-column label="操作类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_history_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="信息" align="center" prop="message" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:userHistory:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:userHistory:remove']"
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

    <!-- 添加或修改用户操作历史记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联用户Id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入关联用户Id" />
        </el-form-item>
        <el-form-item label="关联活动Id" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入关联活动Id" />
        </el-form-item>
        <el-form-item label="操作类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择操作类型">
            <el-option
              v-for="dict in dict.type.user_history_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="信息" prop="message">
          <el-input v-model="form.message" placeholder="请输入信息" />
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
import { listUserHistory, getUserHistory, delUserHistory, addUserHistory, updateUserHistory } from "@/api/system/userHistory";

export default {
  name: "UserHistory",
  dicts: ['user_history_type'],
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
      // 用户操作历史记录表格数据
      userHistoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        activityId: undefined,
        type: undefined,
        message: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        historyId: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "关联用户Id不能为空", trigger: "blur" }
        ],
        activityId: [
          { required: true, message: "关联活动Id不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "操作类型不能为空", trigger: "change" }
        ],
        message: [
          { required: true, message: "信息不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "记录创建时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户操作历史记录列表 */
    getList() {
      this.loading = true;
      listUserHistory(this.queryParams).then(response => {
        this.userHistoryList = response.rows;
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
        historyId: undefined,
        userId: undefined,
        activityId: undefined,
        type: undefined,
        message: undefined,
        createTime: undefined
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.historyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户操作历史记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const historyId = row.historyId || this.ids
      getUserHistory(historyId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改用户操作历史记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.historyId != null) {
            updateUserHistory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserHistory(this.form).then(response => {
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
      const historyIds = row.historyId || this.ids;
      this.$modal.confirm('是否确认删除用户操作历史记录编号为"' + historyIds + '"的数据项？').then(() => {
        this.loading = true;
        return delUserHistory(historyIds);
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
      this.download('system/userHistory/export', {
        ...this.queryParams
      }, `userHistory_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
