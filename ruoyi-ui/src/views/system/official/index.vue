<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="来自谁的消息" prop="fromUserId">
        <el-input
          v-model="queryParams.fromUserId"
          placeholder="请输入来自谁的消息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="给谁发的消息" prop="toUserId">
        <el-input
          v-model="queryParams.toUserId"
          placeholder="请输入给谁发的消息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否已读" prop="isRead">
        <el-input
          v-model="queryParams.read"
          placeholder="请输入是否已读"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来自那场组队的消息" prop="groupId">
        <el-input
          v-model="queryParams.groupId"
          placeholder="请输入来自那场组队的消息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来自那场活动的消息" prop="activityId">
        <el-input
          v-model="queryParams.activityId"
          placeholder="请输入来自那场活动的消息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-date-picker clearable
          v-model="queryParams.updateTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择更新时间">
        </el-date-picker>
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
          v-hasPermi="['system:official:add']"
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
          v-hasPermi="['system:official:edit']"
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
          v-hasPermi="['system:official:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:official:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="officialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="officialId" v-if="true"/>
      <el-table-column label="来自谁的消息" align="center" prop="fromUserId" />
      <el-table-column label="给谁发的消息" align="center" prop="toUserId" />
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="主体消息" align="center" prop="content" />
      <el-table-column label="是否已读" align="center" prop="isRead" />
      <el-table-column label="来自那场组队的消息" align="center" prop="groupId" />
      <el-table-column label="来自那场活动的消息" align="center" prop="activityId" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:official:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:official:remove']"
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

    <!-- 添加或修改官方消息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="来自谁的消息" prop="fromUserId">
          <el-input v-model="form.fromUserId" placeholder="请输入来自谁的消息" />
        </el-form-item>
        <el-form-item label="给谁发的消息" prop="toUserId">
          <el-input v-model="form.toUserId" placeholder="请输入给谁发的消息" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="主体消息">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="是否已读" prop="isRead">
          <el-input v-model="form.read" placeholder="请输入是否已读" />
        </el-form-item>
        <el-form-item label="来自那场组队的消息" prop="groupId">
          <el-input v-model="form.groupId" placeholder="请输入来自那场组队的消息" />
        </el-form-item>
        <el-form-item label="来自那场活动的消息" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入来自那场活动的消息" />
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
import { listOfficial, getOfficial, delOfficial, addOfficial, updateOfficial } from "@/api/system/official";

export default {
  name: "Official",
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
      // 官方消息表格数据
      officialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fromUserId: undefined,
        toUserId: undefined,
        title: undefined,
        content: undefined,
        read: undefined,
        groupId: undefined,
        activityId: undefined,
        createTime: undefined,
        updateTime: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        officialId: [
          { required: true, message: "ID不能为空", trigger: "blur" }
        ],
        fromUserId: [
          { required: true, message: "来自谁的消息不能为空", trigger: "blur" }
        ],
        toUserId: [
          { required: true, message: "给谁发的消息不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "主体消息不能为空", trigger: "blur" }
        ],
        read: [
          { required: true, message: "是否已读不能为空", trigger: "blur" }
        ],
        groupId: [
          { required: true, message: "来自那场组队的消息不能为空", trigger: "blur" }
        ],
        activityId: [
          { required: true, message: "来自那场活动的消息不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询官方消息列表 */
    getList() {
      this.loading = true;
      listOfficial(this.queryParams).then(response => {
        this.officialList = response.rows;
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
        officialId: undefined,
        fromUserId: undefined,
        toUserId: undefined,
        title: undefined,
        content: undefined,
        read: undefined,
        groupId: undefined,
        activityId: undefined,
        createTime: undefined,
        updateTime: undefined
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
      this.ids = selection.map(item => item.officialId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加官方消息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const officialId = row.officialId || this.ids
      getOfficial(officialId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改官方消息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.officialId != null) {
            updateOfficial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOfficial(this.form).then(response => {
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
      const officialIds = row.officialId || this.ids;
      this.$modal.confirm('是否确认删除官方消息编号为"' + officialIds + '"的数据项？').then(() => {
        this.loading = true;
        return delOfficial(officialIds);
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
      this.download('system/official/export', {
        ...this.queryParams
      }, `official_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
