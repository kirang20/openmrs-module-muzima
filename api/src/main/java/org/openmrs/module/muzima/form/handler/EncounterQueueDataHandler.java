/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.muzima.form.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzima.api.service.DataService;
import org.openmrs.module.muzima.form.EncounterQueueData;
import org.openmrs.module.muzima.model.ArchiveData;
import org.openmrs.module.muzima.model.QueueData;
import org.openmrs.module.muzima.model.handler.QueueDataHandler;

import java.util.Date;

/**
 */
@Handler(supports = EncounterQueueData.class, order = 50)
public class EncounterQueueDataHandler implements QueueDataHandler {

    private final Log log = LogFactory.getLog(EncounterQueueDataHandler.class);

    private static final String ARCHIVING_MESSAGE = "Encounter form queue data archived!";

    @Override
    public void process(final QueueData queueData) {
        log.info("Processing encounter form data: " + queueData.getUuid());

        DataService dataService = Context.getService(DataService.class);
        EncounterQueueData encounterQueueData = (EncounterQueueData) queueData;

        ArchiveData archiveData = new ArchiveData(encounterQueueData);
        archiveData.setDateArchived(new Date());
        archiveData.setObjectType(EncounterQueueData.DISCRIMINATOR_VALUE);
        archiveData.setMessage(EncounterQueueDataHandler.ARCHIVING_MESSAGE);
        dataService.saveArchiveData(archiveData);

        dataService.purgeQueueData(queueData);
    }
}
