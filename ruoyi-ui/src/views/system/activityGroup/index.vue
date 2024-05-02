<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="活动ID" prop="activityId">
        <el-input
          v-model="queryParams.activityId"
          placeholder="请输入活动ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动组队发起人ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入活动组队发起人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动主题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入活动主题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动组队所缴纳的保证金" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入活动组队所缴纳的保证金"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="买单方式" prop="groupType">
        <el-select v-model="queryParams.groupType" placeholder="请选择买单方式" clearable>
          <el-option
            v-for="dict in dict.type.group_pay_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="活动地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入活动地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="一起约定好的时间" prop="activityTime">
        <el-date-picker clearable
          v-model="queryParams.activityTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择一起约定好的时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="权限" prop="auth">
        <el-input
          v-model="queryParams.auth"
          placeholder="请输入权限"
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
          v-hasPermi="['system:activityGroup:add']"
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
          v-hasPermi="['system:activityGroup:edit']"
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
          v-hasPermi="['system:activityGroup:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:activityGroup:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="activityGroupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="组队ID" align="center" prop="groupId" v-if="true"/>
      <el-table-column label="活动ID" align="center" prop="activityId" />
      <el-table-column label="活动组队发起人ID" align="center" prop="userId" />
      <el-table-column label="活动主题" align="center" prop="title" />
      <el-table-column label="活动组队所缴纳的保证金" align="center" prop="money" />
      <el-table-column label="买单方式" align="center" prop="groupType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.group_pay_type" :value="scope.row.groupType"/>
        </template>
      </el-table-column>
      <el-table-column label="活动地址" align="center" prop="address" />
      <el-table-column label="一起约定好的时间" align="center" prop="activityTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activityTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="权限" align="center" prop="auth">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.group_auth" :value="scope.row.auth"/>
        </template>
      </el-table-column>
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
            v-hasPermi="['system:activityGroup:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:activityGroup:remove']"
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

    <!-- 添加或修改活动组队对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="活动ID" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入活动ID" />
        </el-form-item>
        <el-form-item label="活动组队发起人ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入活动组队发起人ID" />
        </el-form-item>
        <el-form-item label="活动主题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动主题" />
        </el-form-item>
        <el-form-item label="活动组队所缴纳的保证金" prop="money">
          <el-input v-model="form.money" placeholder="请输入活动组队所缴纳的保证金" />
        </el-form-item>
        <el-form-item label="买单方式" prop="groupType">
          <el-select v-model="form.groupType" placeholder="请选择买单方式">
            <el-option
              v-for="dict in dict.type.group_pay_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入活动地址" />
        </el-form-item>
        <el-form-item label="一起约定好的时间" prop="activityTime">
          <el-date-picker clearable
            v-model="form.activityTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择一起约定好的时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="权限" prop="auth">
          <el-input v-model="form.auth" placeholder="请输入权限" />
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
import { listActivityGroup, getActivityGroup, delActivityGroup, addActivityGroup, updateActivityGroup } from "@/api/system/activityGroup";

export default {
  name: "ActivityGroup",
  dicts: ['group_pay_type'],
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
      // 活动组队表格数据
      activityGroupList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        activityId: undefined,
        userId: undefined,
        title: undefined,
        money: undefined,
        groupType: undefined,
        address: undefined,
        activityTime: undefined,
        auth: undefined,
        createTime: undefined,
        updateTime: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        groupId: [
          { required: true, message: "组队ID不能为空", trigger: "blur" }
        ],
        activityId: [
          { required: true, message: "活动ID不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "活动组队发起人ID不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "活动主题不能为空", trigger: "blur" }
        ],
        money: [
          { required: true, message: "活动组队所缴纳的保证金不能为空", trigger: "blur" }
        ],
        groupType: [
          { required: true, message: "买单方式不能为空", trigger: "change" }
        ],
        address: [
          { required: true, message: "活动地址不能为空", trigger: "blur" }
        ],
        activityTime: [
          { required: true, message: "一起约定好的时间不能为空", trigger: "blur" }
        ],
        auth: [
          { required: true, message: "权限不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询活动组队列表 */
    getList() {
      this.loading = true;
      listActivityGroup(this.queryParams).then(response => {
        this.activityGroupList = response.rows;
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
        groupId: undefined,
        activityId: undefined,
        userId: undefined,
        title: undefined,
        money: undefined,
        groupType: undefined,
        address: undefined,
        activityTime: undefined,
        auth: undefined,
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
      this.ids = selection.map(item => item.groupId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加活动组队";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const groupId = row.groupId || this.ids
      getActivityGroup(groupId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改活动组队";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.groupId != null) {
            updateActivityGroup(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addActivityGroup(this.form).then(response => {
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
      const groupIds = row.groupId || this.ids;
      this.$modal.confirm('是否确认删除活动组队编号为"' + groupIds + '"的数据项？').then(() => {
        this.loading = true;
        return delActivityGroup(groupIds);
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
      this.download('system/activityGroup/export', {
        ...this.queryParams
      }, `activityGroup_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
