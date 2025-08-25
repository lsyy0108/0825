專案簡介<br>

這個 Java 桌面應用程式是一個 藥局管理系統，提供員工登入、會員管理、藥品管理、銷售管理與報表功能。<br>
使用 Swing GUI，透過 DAO/Service 層與資料庫互動，支援完整 CRUD 功能與報表產生。程式已打包成 JAR，可直接執行。<br>
<br>
核心功能<br>

員工登入與權限管理<br>

會員、藥品、銷售資料管理（CRUD）<br>

銷售報表生成（含 Excel 或圖表支援）<br>

Swing GUI 直覺化操作<br>

DAO 與 Service 層分離，結構清晰<br>
<br>
模組說明<br>

controller/：GUI 控制器，負責介面操作與事件處理<br>

dao/：資料存取介面 (CRUD 定義)<br>

dao/impl/：DAO 實作，實際操作資料庫<br>

model/：資料模型，如員工、會員、藥品、銷售<br>

service/：業務邏輯介面<br>

service/impl/：業務邏輯實作，串接 DAO 與 Controller<br>

util/：工具類，包含資料庫連線與輔助方法<br>
<br>

HomeWork.jar<br>
│<br>
│<br>
├─ controller/                   # GUI 控制器<br>
│   ├─ EmployeeLogin.class        # 員工登入介面<br>
│   ├─ EmployeeUI.class           # 員工主介面<br>
│   ├─ EmployeesUI.class          # 員工管理介面<br>
│   ├─ DrugsUI.class              # 藥品管理介面<br>
│   ├─ MembersUI.class            # 會員管理介面<br>
│   ├─ SalesUI.class              # 銷售管理介面<br>
│   └─ SalesReportUI.class        # 銷售報表介面<br>
│   <br>
│<br>
├─ dao/                          # 資料存取介面<br>
│   ├─ EmployeeDAO.class<br>
│   ├─ MemberDAO.class<br>
│   ├─ SaleDAO.class<br>
│   └─ DrugDAO.class<br>
│   └─impl/                     # DAO 實作<br>
│  　 ├─ EmployeeDAOImpl.class<br>
│  　 ├─ MemberDAOImpl.class<br>
│  　 ├─ SalesDAOImpl.class<br>
│  　 └─ DrugDAOImpl.class<br>
│<br>
├─ model/                        # 資料模型<br>
│   ├─ Employee.class<br>
│   ├─ Member.class<br>
│   ├─ Sale.class<br>
│   └─ Drug.class<br>
│<br>
├─ service/                      # 業務邏輯介面<br>
│   ├─ EmployeeService.class<br>
│   ├─ MemberService.class<br>
│   ├─ SaleService.class<br>
│   └─ DrugService.class<br>
│   └─service/impl/                 # Service 實作<br>
│  　 ├─ EmployeeServiceImpl.class<br>
│  　 ├─ MemberServiceImpl.class<br>
│ 　  ├─ SaleServiceImpl.class<br>
│ 　  └─ DrugServiceImpl.class<br>
│<br>
└─ util/                         # 工具類<br>
    ├─CodeGeneratorUtil          # 訂單生成<br>
    └─ DBUtil.class              # 資料庫連線工具<br>
