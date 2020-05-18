package com.ftn.suites;

import com.ftn.services.manifestationSector.ManifestationSectorIntegrationTest;
import com.ftn.services.manifestationSector.ManifestationSectorUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ManifestationSectorUnitTest.class,
        ManifestationSectorIntegrationTest.class
})
public class SuiteManSector {
}
