/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.quickaccess;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Ignore("These test have been failing a lot, they are disabled until they are fixed to be more stable. See bug 551153")
@RunWith(Suite.class)
@Suite.SuiteClasses({ CamelUtilTest.class, QuickAccessDialogTest.class, ShellClosingTest.class,
		ContentMatchesTest.class })
public class QuickAccessTestSuite {
}
