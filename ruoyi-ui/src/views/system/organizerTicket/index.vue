<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联主办方ID" prop="organizerId">
        <el-input
          v-model="queryParams.organizerId"
          placeholder="请输入关联主办方ID"
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
          v-hasPermi="['system:organizerTicket:add']"
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
          v-hasPermi="['system:organizerTicket:edit']"
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
          v-hasPermi="['system:organizerTicket:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:organizerTicket:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="organizerTicketList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="organizerTicketId" v-if="true"/>
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="二维码图片" align="center" prop="qrImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.qrImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="logo图" align="center" prop="logoImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.logoImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="关联主办方ID" align="center" prop="organizerId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:organizerTicket:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:organizerTicket:remove']"
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

    <!-- 添加或修改主办方票务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="二维码图片" prop="qrImage">
          <image-upload v-model="form.qrImage"/>
        </el-form-item>
        <el-form-item label="logo图" prop="logoImage">
          <image-upload v-model="form.logoImage"/>
        </el-form-item>
        <el-form-item label="关联主办方ID" prop="organizerId">
          <el-input v-model="form.organizerId" placeholder="请输入关联主办方ID" />
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
import { listOrganizerTicket, getOrganizerTicket, delOrganizerTicket, addOrganizerTicket, updateOrganizerTicket } from "@/api/system/organizerTicket";

export default {
  name: "OrganizerTicket",
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
      // 主办方票务表格数据
      organizerTicketList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        qrImage: undefined,
        logoImage: undefined,
        organizerId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        organizerTicketId: [
          { required: true, message: "ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        qrImage: [
          { required: true, message: "二维码图片不能为空", trigger: "blur" }
        ],
        logoImage: [
          { required: true, message: "logo图不能为空", trigger: "blur" }
        ],
        organizerId: [
          { required: true, message: "关联主办方ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询主办方票务列表 */
    getList() {
      this.loading = true;
      listOrganizerTicket(this.queryParams).then(response => {
        this.organizerTicketList = response.rows;
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
        organizerTicketId: undefined,
        name: undefined,
        qrImage: undefined,
        logoImage: undefined,
        organizerId: undefined,
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.organizerTicketId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加主办方票务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const organizerTicketId = row.organizerTicketId || this.ids
      getOrganizerTicket(organizerTicketId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改主办方票务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.organizerTicketId != null) {
            updateOrganizerTicket(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOrganizerTicket(this.form).then(response => {
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
      const organizerTicketIds = row.organizerTicketId || this.ids;
      this.$modal.confirm('是否确认删除主办方票务编号为"' + organizerTicketIds + '"的数据项？').then(() => {
        this.loading = true;
        return delOrganizerTicket(organizerTicketIds);
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
      this.download('system/organizerTicket/export', {
        ...this.queryParams
      }, `organizerTicket_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
