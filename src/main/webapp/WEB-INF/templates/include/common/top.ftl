<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top navbar-top">
    <div class="container pl-sm-0 pr-sm-0">
        <div><a class="navbar-brand mr-1 mr-md-2" href="/">Simple Java Boards</a></div>
        <button class="navbar-toggler navbar-toggler-right px-2" type="button" data-toggle="collapse"
                data-target="#navbar-responsive" aria-controls="navbar-responsive" aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbar-responsive">
            <ul class="navbar-nav ml-auto">

                <@security.authorize access="! isAuthenticated()">
                    <li class="nav-item dropdown">
                        <a class="nav-link" title="Sign Up" href="/sign-up">
                            <i class="fas fa-fw fa-sign-in-alt"></i>
                            <span class="d-lg-none d-md-none">Sign Up</span>
                        </a>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link" title="Reset password" href="/reset-password">
                            <i class="fas fa-fw fa-unlock"></i>
                            <span class="d-lg-none d-md-none">Reset password</span>
                        </a>
                    </li>

                    <li class="nav-item dropdown pr-2">
                        <a class="nav-link" id="login-dropdown" title="Sign In" href="/sign-in">
                            <i class="fas fa-fw fa-key"></i>
                            <span class="d-lg-none d-md-none">Sign In</span>
                        </a>
                    </li>
                </@security.authorize>
                <@security.authorize access="isAuthenticated()">
<#--
TODO: private messages and notifications
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" id="posts-dropdown" href="#" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-fw fa-envelope"></i>
                            <span class="d-lg-none d-md-none">Private posts
                                <span class="badge badge-pill badge-primary">12 New</span>
                            </span>
                            <span class="indicator text-primary d-none d-md-block d-lg-block">
                                <i class="fas fa-fw fa-circle"></i>
                            </span>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="posts-dropdown">
                            <h6 class="dropdown-header mb-2">New Messages:</h6>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2" href="#">
                                <strong>David Miller</strong>
                                <span class="small float-right text-muted">11:21 AM</span>
                                <div class="dropdown-post small">Hey there! This new version of SB Admin is pretty
                                    awesome! These posts clip off when they reach the end of the box so they don't
                                    overflow over to the sides!
                                </div>
                            </a>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2" href="#">
                                <strong>Jane Smith</strong>
                                <span class="small float-right text-muted">11:21 AM</span>
                                <div class="dropdown-post small">I was wondering if you could meet for an appointment at
                                    3:00 instead of 4:00. Thanks!
                                </div>
                            </a>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2" href="#">
                                <strong>John Doe</strong>
                                <span class="small float-right text-muted">11:21 AM</span>
                                <div class="dropdown-post small">I've sent the final files over to you for review. When
                                    you're able to sign off of them let me know and we can discuss distribution.
                                </div>
                            </a>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2 small" href="#">View all posts</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" id="notifications-dropdown" href="#" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-fw fa-bell"></i>
                            <span class="d-md-none d-lg-none">Notifications
                                <span class="badge badge-pill badge-danger">6 New</span>
                            </span>
                            <span class="indicator text-danger d-none d-md-block d-lg-block">
                                <i class="fas fa-fw fa-circle"></i>
                            </span>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="notifications-dropdown">
                            <h6 class="dropdown-header mb-2">New Notifications:</h6>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2" href="#">
                                <span class="text-success">
                                    <strong>
                                        <i class="fas fa-long-arrow-alt-up fa-fw"></i>Status Update</strong>
                                </span>
                                <span class="small float-right text-muted">11:21 AM</span>
                                <div class="dropdown-post small">This is an automated server response post. All
                                    systems are online.
                                </div>
                            </a>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2" href="#">
                                <span class="text-danger">
                                    <strong>
                                        <i class="fas fa-long-arrow-alt-down fa-fw"></i>Status Update</strong>
                                </span>
                                <span class="small float-right text-muted">11:21 AM</span>
                                <div class="dropdown-post small">This is an automated server response post. All
                                    systems are online.
                                </div>
                            </a>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2" href="#">
                                <span class="text-success">
                                    <strong>
                                        <i class="fas fa-long-arrow-alt-up fa-fw"></i>Status Update</strong>
                                </span>
                                <span class="small float-right text-muted">11:21 AM</span>
                                <div class="dropdown-post small">This is an automated server response post. All
                                    systems are online.
                                </div>
                            </a>
                            <div class="dropdown-divider m-0"></div>
                            <a class="dropdown-item py-2 small" href="#">View all alerts</a>
                        </div>
                    </li>
-->


                    <li class="nav-item">
                        <div class="text-white p-2">${userPrincipal.nickName}, welcome!</div>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link mr-md-2" title="Profile" href="/profile/${userPrincipal.id}/overview">
                            <i class="fas fa-fw fa-user-alt"></i>
                            <span class="d-md-none d-lg-none">Profile</span>
                        </a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link mr-md-2" title="Logout" href="/sign-out">
                            <i class="fas fa-fw fa-sign-out-alt"></i>
                            <span class="d-md-none d-lg-none">Logout</span>
                        </a>
                    </li>
                </@security.authorize>
                <li class="nav-item pt-2 pb-1 py-md-0">
                    <form class="form-inline">
                        <div class="input-group">
                            <input disabled class="form-control" type="text" placeholder="Search..." />
                            <span class="input-group-append">
                                <button disabled class="btn btn-primary" type="button">
                                    <i class="fas fa-search"></i>
                                </button>
                            </span>
                        </div>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>