<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="Dự án FlowerShop" />
                <meta name="author" content="IT" />
                <title>Dashboard </title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Target</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Target</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-12 mx-auto">
                                            <div class="d-flex justify-content-between">
                                                <h3>Table Target</h3>
                                                <a href="/admin/target/create" class="btn btn-primary">Create a
                                                    Target</a>
                                            </div>

                                            <hr />
                                            <table class=" table table-bordered table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Name</th>
                                                        <th>Description</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="target" items="${targets}">
                                                        <tr>
                                                            <th>${target.id}</th>
                                                            <td>${target.name}</td>
                                                            <td>${target.description}</td>
                                                            <td>
                                                                <!-- <a href="/admin/category/${category.id}"
                                                                    class="btn btn-success">View</a> -->
                                                                <a href="/admin/target/update/${category.id}"
                                                                    class="btn btn-warning  mx-2">Update</a>
                                                                <!-- <a href="/admin/category/delete/${category.id}"
                                                                    class="btn btn-danger">Delete</a> -->
                                                                <a href="/admin/target/delete/${category.id}" class="btn btn-danger" onclick="return confirmDelete();">Delete</a>
                                                            </td>
                                                        </tr>

                                                    </c:forEach>

                                                </tbody>
                                            </table>
                                            
                                        </div>

                                    </div>

                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>

                <script type="text/javascript">
                    function confirmDelete() {
                        return confirm('Bạn có chắc chắn muốn xóa muc tieu này không?');
                    }
                </script>
            </body>

            </html>