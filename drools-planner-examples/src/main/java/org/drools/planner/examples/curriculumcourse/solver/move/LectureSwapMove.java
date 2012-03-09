/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.planner.examples.curriculumcourse.solver.move;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.curriculumcourse.domain.Lecture;
import org.drools.planner.examples.curriculumcourse.domain.Period;
import org.drools.planner.examples.curriculumcourse.domain.Room;

public class LectureSwapMove implements Move {

    private Lecture leftLecture;
    private Lecture rightLecture;

    public LectureSwapMove(Lecture leftLecture, Lecture rightLecture) {
        this.leftLecture = leftLecture;
        this.rightLecture = rightLecture;
    }

    public boolean isMoveDoable(WorkingMemory workingMemory) {
        return !(ObjectUtils.equals(leftLecture.getPeriod(), rightLecture.getPeriod())
                && ObjectUtils.equals(leftLecture.getRoom(), rightLecture.getRoom()));
    }

    public Move createUndoMove(WorkingMemory workingMemory) {
        return new LectureSwapMove(rightLecture, leftLecture);
    }

    public void doMove(WorkingMemory workingMemory) {
        Period oldLeftPeriod = leftLecture.getPeriod();
        Period oldRightPeriod = rightLecture.getPeriod();
        Room oldLeftRoom = leftLecture.getRoom();
        Room oldRightRoom = rightLecture.getRoom();
        if (oldLeftPeriod.equals(oldRightPeriod)) {
            CurriculumCourseMoveHelper.moveRoom(workingMemory, leftLecture, oldRightRoom);
            CurriculumCourseMoveHelper.moveRoom(workingMemory, rightLecture, oldLeftRoom);
        } else if (oldLeftRoom.equals(oldRightRoom)) {
            CurriculumCourseMoveHelper.movePeriod(workingMemory, leftLecture, oldRightPeriod);
            CurriculumCourseMoveHelper.movePeriod(workingMemory, rightLecture, oldLeftPeriod);
        } else {
            CurriculumCourseMoveHelper.moveLecture(workingMemory, leftLecture, oldRightPeriod, oldRightRoom);
            CurriculumCourseMoveHelper.moveLecture(workingMemory, rightLecture, oldLeftPeriod, oldLeftRoom);
        }
    }

    public Collection<? extends Object> getPlanningEntities() {
        return Arrays.asList(leftLecture, rightLecture);
    }

    public Collection<? extends Object> getPlanningValues() {
        return Arrays.<Object>asList(leftLecture.getPeriod(), leftLecture.getRoom(),
                rightLecture.getPeriod(), rightLecture.getRoom());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof LectureSwapMove) {
            LectureSwapMove other = (LectureSwapMove) o;
            return new EqualsBuilder()
                    .append(leftLecture, other.leftLecture)
                    .append(rightLecture, other.rightLecture)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(leftLecture)
                .append(rightLecture)
                .toHashCode();
    }

    public String toString() {
        return leftLecture + " <=> " + rightLecture;
    }

}
