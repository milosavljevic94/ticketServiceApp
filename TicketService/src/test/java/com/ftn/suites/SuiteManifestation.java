package com.ftn.suites;

import com.ftn.controllers.ManifestationControllerTest;
import com.ftn.services.manifestation.ManifestationServiceIntegrationTest;
import com.ftn.services.manifestation.ManifestationServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ManifestationServiceUnitTest.class,
        ManifestationServiceIntegrationTest.class,
        ManifestationControllerTest.class
})
public class SuiteManifestation {
}
