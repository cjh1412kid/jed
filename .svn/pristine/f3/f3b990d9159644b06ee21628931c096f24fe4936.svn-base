/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.nuite.service;

import io.nuite.datasources.DataSourceNames;
import io.nuite.datasources.annotation.DataSource;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试多数据源
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.1.0 2018-01-28
 */
@Service
public class DataSourceTestService {
    @Autowired
    private BaseUserService baseUserService;

    public BaseUserEntity queryUser(Long userId) {
        return baseUserService.selectById(userId);
    }

    @DataSource(name = DataSourceNames.DEFAULT)
    public BaseUserEntity queryUser2(Long userId) {
        return baseUserService.selectById(userId);
    }
}
