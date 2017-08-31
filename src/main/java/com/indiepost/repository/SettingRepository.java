package com.indiepost.repository;

import com.indiepost.model.Setting;

/**
 * Created by jake on 8/31/17.
 */
public interface SettingRepository {
    Setting get();

    void update(Setting setting);
}
