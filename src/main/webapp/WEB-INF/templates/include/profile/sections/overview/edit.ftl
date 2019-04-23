<#import "/spring.ftl" as spring />
                                        <h3 class="font-weight-light pb-2">Overview - edit</h3>
            <form role="document" id="edit-overview-form" class="mt-3" method="POST" action="${currentPageBase}/overview/edit">
                <div class="card">
                    <div class="card-body pb-0">
                        <div class="row form-group">
                            <label for="name" class="col-form-label col-sm-3">Name:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-user"></i></div>
                                    </div>
                                    <@spring.formInput "editOverviewForm.name" "class=\"form-control\" placeholder=\"Your name\" required" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "name" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="surname" class="col-form-label col-sm-3">Surname:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-user"></i></div>
                                    </div>
                                    <@spring.formInput "editOverviewForm.surname" "class=\"form-control\" placeholder=\"Your surname\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "surname" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="gender" class="col-form-label col-sm-3">Gender:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-venus-mars"></i></div>
                                    </div>
                                    <@spring.formSingleSelect "editOverviewForm.gender" genderList "class=\"form-control custom-select\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "gender" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="birthDate" class="col-form-label col-sm-3">Birthdate:</label>
                            <div class="col-sm-9">
                                <div class="input-group date" id="datePicker" data-target-input="nearest">
                                    <div class="input-group-prepend" data-target="#datePicker" data-toggle="datetimepicker">
                                        <div class="input-group-text"><i class="fa fa-fw fa-calendar"></i></div>
                                    </div>
                                    <input type="text" id="birthDate" name="birthDate" value="<#if editOverviewForm.birthDate??>${editOverviewForm.birthDate}</#if>" class="form-control datetimepicker-input" data-target="#datePicker"/>
                                </div>
                                <@showValidationErrors "editOverviewForm" "birthDate" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="location" class="col-form-label col-sm-3">Location:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-thumbtack"></i></div>
                                    </div>
                                    <@spring.formInput "editOverviewForm.location" "class=\"form-control\" placeholder=\"Your location\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "location" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="site" class="col-form-label col-sm-3">Web site:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-blog"></i></div>
                                    </div>
                                    <@spring.formInput "editOverviewForm.site" "class=\"form-control\" placeholder=\"Your web site\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "site" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="showOnline" class="col-form-label col-sm-3">Show online:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-eye-slash"></i></div>
                                    </div>
                                    <@spring.formSingleSelect "editOverviewForm.showOnline" activeList "class=\"form-control custom-select\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "showOnline" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="hideEmail" class="col-form-label col-sm-3">Hide email:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-eye-slash"></i></div>
                                    </div>
                                    <@spring.formSingleSelect "editOverviewForm.hideEmail" activeList "class=\"form-control custom-select\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "hideEmail" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="hideBirthdate" class="col-form-label col-sm-3">Hide birthdate:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-eye-slash"></i></div>
                                    </div>
                                    <@spring.formSingleSelect "editOverviewForm.hideBirthdate" activeList "class=\"form-control custom-select\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "hideBirthdate" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="signature" class="col-form-label col-sm-3">Signature:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-signature"></i></div>
                                    </div>
                                    <@spring.formInput "editOverviewForm.signature" "class=\"form-control\" placeholder=\"Your signature\"" />
                                </div>
                                <@showValidationErrors "editOverviewForm" "signature" />
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "editOverviewForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary edit-overview-form-submit">Save</button>
                    <a class="btn btn-primary ml-2" href="${currentPageBase}/overview">Cancel</a>
                </div>
            </form>
        <script type="text/javascript" src="/resources/js/overview.js" defer></script>
