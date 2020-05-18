package com.ftn.suites;

import com.ftn.services.manifestationDay.ManifestationDaysIntegrationTest;
import com.ftn.services.manifestationDay.ManifestationDaysUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ManifestationDaysUnitTest.class,
        ManifestationDaysIntegrationTest.class
    })
public class SuiteManifestationDay {
}
