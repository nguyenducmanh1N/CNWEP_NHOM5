<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content=" Dự án " />
                <meta name="author" content="IT" />
                <title>Update Factory</title>
                <link href="/css/styles.css" rel="stylesheet" />
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Factory</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item"><a href="/admin/factory">Factory</a></li>
                                    <li class="breadcrumb-item active">Update</li>
                                </ol>
                                <div class=" mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h3>Update Factory</h3>
                                            <hr />
                                            <form:form method="post" action="/admin/factory/update" class="row"
                                                enctype="multipart/form-data" modelAttribute="newFactory">

                                                <!-- Hidden Field for ID -->
                                                <div class="mb-3" style="display: none;">
                                                    <label class="form-label">Id:</label>
                                                    <form:input type="text" class="form-control" path="id" />
                                                </div>

                                                <c:set var="errorName">
                                                    <form:errors path="name" cssClass="invalid-feedback" />
                                                </c:set>
                                                <c:set var="errorDescription">
                                                    <form:errors path="description" cssClass="invalid-feedback" />
                                                </c:set>
                                                
                                                <div class="mb-3 col-12">
                                                    <label class="form-label">Name:</label>
                                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" path="name" />
                                                    ${errorName}
                                                </div>
                                                <div class="mb-3 col-12">
                                                    <label class="form-label">Location :</label>
                                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" path="location" />
                                                    ${errorName}
                                                </div>
                                                
                                                <div class="mb-3 col-12">
                                                    <label class="form-label">Description:</label>
                                                    <form:textarea class="form-control ${not empty errorDescription ? 'is-invalid' : ''}" path="description" rows="4" />

                                                    ${errorDescription}
                                                </div>
                                                <div class="mb-3 col-12">
                                                    <label class="form-label">Phone:</label>
                                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" path="phone" />
                                                
                                                </div>
                                                <div class="mb-3 col-12">
                                                    <label class="form-label">Gmail:</label>
                                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" path="gmail" />
                                                
                                                </div>
                                                <div class="mb-3 col-12">
                                                    <label class="form-label">Url :</label>
                                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" path="url" />
                                                
                                                </div>


                                                <!-- Submit Button -->
                                                <div class="col-12 mb-5">
                                                    <button type="submit" class="btn btn-warning">Update
                                                        Factory</button>
                                                </div>
                                            </form:form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
            </body>

            </html>